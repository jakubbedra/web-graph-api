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
