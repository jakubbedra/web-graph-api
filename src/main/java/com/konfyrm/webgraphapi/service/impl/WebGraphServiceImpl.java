package com.konfyrm.webgraphapi.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.konfyrm.webgraphapi.domain.message.UrlNode;
import com.konfyrm.webgraphapi.domain.model.UrlGraph;
import com.konfyrm.webgraphapi.service.WebGraphService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class WebGraphServiceImpl implements WebGraphService {

    private static final Logger LOGGER = LogManager.getLogger(WebGraphServiceImpl.class);

    @Value("${application.api.json-path}")
    private String filePath;

    private static final Cache<String, UrlGraph> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build();

    private Map<String, List<UrlNode>> webGraphs = new HashMap<>();

    @Override
    public List<UrlNode> getUrlMap(String executionUuid) {
        return webGraphs.getOrDefault(executionUuid, new LinkedList<>());
    }

    @Override
    public void updateUrlMap(String executionUuid, List<UrlNode> urlNodes) {
        if (!webGraphs.containsKey(executionUuid)) {
            webGraphs.put(executionUuid, urlNodes);
        }
        List<UrlNode> oldUrlMap = webGraphs.get(executionUuid);
        urlNodes.stream()
                .filter(node -> !oldUrlMap.contains(node))
                .forEach(oldUrlMap::add);
    }

    @Override
    public UrlGraph getOrCreateGraph(String executionId) {
        if (cache.getIfPresent(executionId) != null) {
            return cache.getIfPresent(executionId);
        }
        List<UrlNode> webGraph = webGraphs.get(executionId);
        Set<String> urls = webGraph.stream().map(UrlNode::getUrl).collect(Collectors.toSet());
        Map<String, Integer> urlsToIndices = new HashMap<>();
        int ind = 0;
        for (String url : urls) {
            urlsToIndices.put(url, ind);
            ind++;
        }

        List<Integer>[] neighbours = new List[urls.size()];
        int m = 0;
        for (int i = 0; i < urls.size(); i++) {
            neighbours[i] = new LinkedList<>();
        }
        for (UrlNode urlNode : webGraph) {
            for (String neighbour : urlNode.getNeighbours()) {
                neighbours[urlsToIndices.get(urlNode.getUrl())].add(urlsToIndices.get(neighbour));
                m++;
            }
        }

        UrlGraph urlGraph = UrlGraph.builder()
                .n(urls.size())
                .m(m)
                .neighbours(neighbours)
                .urlsToIndices(urlsToIndices)
                .build();
        cache.put(executionId, urlGraph);
        return urlGraph;
    }

    @Override
    public UrlGraph getWithDeletedMaxDegreeVertices(String executionUuid, int amount, boolean outDegree) {
        UrlGraph graph = getOrCreateGraph(executionUuid);
        int n = graph.getN();
        List<Integer>[] neighbours = new List[n];
        for (int v = 0; v < n; v++) {
            neighbours[v] = new LinkedList<>();
            neighbours[v].addAll(graph.getNeighbours()[v]);
        }

        Map<String, Integer> urlsToIndices = new HashMap<>(graph.getUrlsToIndices());
        List<Integer> maxDegreeVertices = new ArrayList<>(amount);
        List<Integer> vertices = new ArrayList<>(n);
        for (int v = 0; v < n; v++) {
            vertices.add(v);
        }

        Integer[] inDegrees = outDegree ? null : getVertexInDegrees(neighbours, n);
        Comparator<Integer> comparator = outDegree ?
                Comparator.comparingInt(v -> neighbours[v].size()) :
                Comparator.comparingInt(v -> inDegrees[v]);

        vertices = vertices.stream()
                .sorted(comparator.reversed())
                .limit(amount)
                .collect(Collectors.toList());

        for (int i = 0; i < amount; i++) {
            Integer maxDegreeVertex = vertices.getFirst();
//                    stream()
//                    .filter(v -> !maxDegreeVertices.contains(v))
//                    .max(comparator)
//                    .orElseThrow(() -> new IllegalStateException("Graph for execution: " + executionUuid + " does not contain enough vertices!"));
            maxDegreeVertices.add(maxDegreeVertex);

            for (int v = 0; v < n; v++) {
                neighbours[v] = neighbours[v].stream()
                        .filter(w -> !Objects.equals(w, maxDegreeVertex))
                        .map(w -> (w > maxDegreeVertex ? w-1 : w))
                        .toList();
            }


            String maxDegreeVertexKey = urlsToIndices.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(maxDegreeVertex))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElseThrow();
            urlsToIndices.remove(maxDegreeVertexKey);
            for (int v = maxDegreeVertex + 1; v < n; v++) {
                Integer finalV = v;
                String key = urlsToIndices.entrySet().stream()
                        .filter(entry -> entry.getValue().equals(finalV))
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .orElseThrow();
                urlsToIndices.remove(key);
                urlsToIndices.put(key, v-1);
            }
            vertices = vertices.stream()
                    .filter(w -> w != maxDegreeVertex)
                    .map(w -> (w > maxDegreeVertex ? w-1 : w))
                    .toList();


            for (int v = maxDegreeVertex; v < n-1; v++) {
                neighbours[v] = neighbours[v+1];
            }
            neighbours[n-1] = null;
            n--;
        }

        // todo: nie zmniejszam indeksow w tej liscie max -_-

        int m = 0;
        for (int i = 0; i < n; i++) {
            m += neighbours[i].size();
        }
        return UrlGraph.builder()
                .n(n)
                .m(m)
                .neighbours(neighbours)
                .urlsToIndices(urlsToIndices)
                .build();
    }

    private Integer[] getVertexInDegrees(List<Integer>[] digraph, int n) {
        Integer[] vertexDegrees = new Integer[n];
        Arrays.fill(vertexDegrees, 0);
        for (int v = 0; v < n; v++) {
            for (Integer u : digraph[v]) {
                try {
                    vertexDegrees[u]++;
                } catch (Exception ex) {
                    System.out.println("dupa");
                }
            }
        }
        return vertexDegrees;
    }

    private int inVerticesCount(Integer v, List<Integer>[] neighbours, int n) {
        int inDegree = 0;
        for (int u = 0; u < n; u++) {
            if (u != v && neighbours[u].contains(v)) {
                inDegree++;
            }
        }
        return inDegree;
    }

    @Override
    public UrlGraph getWithDeletedRandomVertices(String executionUuid, int amount) {
        UrlGraph graph = getOrCreateGraph(executionUuid);
        int n = graph.getN();
        List<Integer>[] neighbours = new List[n];
        for (int v = 0; v < n; v++) {
            neighbours[v] = new LinkedList<>();
            neighbours[v].addAll(graph.getNeighbours()[v]);
        }

        Map<String, Integer> urlsToIndices = new HashMap<>(graph.getUrlsToIndices());
        List<Integer> maxDegreeVertices = new ArrayList<>(amount);
        List<Integer> vertices = new ArrayList<>(n);
        for (int v = 0; v < n; v++) {
            vertices.add(v);
        }

        final Random r = new Random();
        Comparator<Integer> comparator = Comparator.comparingInt(x -> r.nextInt()); // todo: exctract method so that only comparator is passed

        vertices = vertices.stream()
                .sorted(comparator.reversed())
                .limit(amount)
                .collect(Collectors.toList());

        for (int i = 0; i < amount; i++) {
            Integer maxDegreeVertex = vertices.getFirst();
//                    stream()
//                    .filter(v -> !maxDegreeVertices.contains(v))
//                    .max(comparator)
//                    .orElseThrow(() -> new IllegalStateException("Graph for execution: " + executionUuid + " does not contain enough vertices!"));
            maxDegreeVertices.add(maxDegreeVertex);

            for (int v = 0; v < n; v++) {
                neighbours[v] = neighbours[v].stream()
                        .filter(w -> !Objects.equals(w, maxDegreeVertex))
                        .map(w -> (w > maxDegreeVertex ? w-1 : w))
                        .toList();
            }


            String maxDegreeVertexKey = urlsToIndices.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(maxDegreeVertex))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElseThrow();
            urlsToIndices.remove(maxDegreeVertexKey);
            for (int v = maxDegreeVertex + 1; v < n; v++) {
                Integer finalV = v;
                String key = urlsToIndices.entrySet().stream()
                        .filter(entry -> entry.getValue().equals(finalV))
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .orElseThrow();
                urlsToIndices.remove(key);
                urlsToIndices.put(key, v-1);
            }
            vertices = vertices.stream()
                    .filter(w -> w != maxDegreeVertex)
                    .map(w -> (w > maxDegreeVertex ? w-1 : w))
                    .toList();


            for (int v = maxDegreeVertex; v < n-1; v++) {
                neighbours[v] = neighbours[v+1];
            }
            neighbours[n-1] = null;
            n--;
        }

        int m = 0;
        for (int i = 0; i < n; i++) {
            m += neighbours[i].size();
        }
        return UrlGraph.builder()
                .n(n)
                .m(m)
                .neighbours(neighbours)
                .urlsToIndices(urlsToIndices)
                .build();
    }

    @Override
    public void exportJson(String executionId) {
        UrlGraph graph = getOrCreateGraph(executionId);
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(filePath + "/" + executionId + ".json"), graph);
        } catch (IOException e) {
            LOGGER.warn("Error while exporting graph for execution " + executionId + ": " + e.getMessage());
        }
    }

    @Override
    public void importJson(String executionId) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            UrlGraph graph = mapper.readValue(new File(filePath + "/" + executionId + ".json"), UrlGraph.class);
            cache.put(executionId, graph);
        } catch (IOException e) {
            LOGGER.warn("Error while reading graph file for execution " + executionId + ": " + e.getMessage());
        }
    }

}
