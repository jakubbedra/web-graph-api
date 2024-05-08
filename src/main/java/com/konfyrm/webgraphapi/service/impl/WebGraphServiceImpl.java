package com.konfyrm.webgraphapi.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.konfyrm.webgraphapi.domain.model.UrlGraph;
import com.konfyrm.webgraphapi.service.WebGraphService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class WebGraphServiceImpl implements WebGraphService {

    private static final Cache<String, UrlGraph> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build();

    private Map<String, Map<String, Set<String>>> webGraphs = new HashMap<>();

    @Override
    public Map<String, Set<String>> getUrlMap(String executionUuid) {
        return webGraphs.get(executionUuid);
    }

    @Override
    public void updateUrlMap(String executionUuid, Map<String, Set<String>> urlMap) {
        if (!webGraphs.containsKey(executionUuid)) {
            webGraphs.put(executionUuid, urlMap);
        }
        Map<String, Set<String>> oldUrlMap = webGraphs.get(executionUuid);
        urlMap.keySet().stream()
                .filter(url -> !oldUrlMap.containsKey(url))
                .forEach(url -> oldUrlMap.put(url, urlMap.get(url)));
    }

    @Override
    public UrlGraph getOrCreateGraph(String executionId) {
        if (cache.getIfPresent(executionId) != null) {
            return cache.getIfPresent(executionId);
        }
        Map<String, Set<String>> webGraph = webGraphs.get(executionId);
        Set<String> urls = new HashSet<>(webGraph.keySet());
        webGraph.values().stream().flatMap(Collection::stream).forEach(urls::add);
        Map<String, Integer> urlsToIndices = new HashMap<>();
        int ind = 0;
        for (String url : urls) {
            urlsToIndices.put(url, ind);
            ind++;
        }

        int[][] matrix = new int[urls.size()][urls.size()];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[i][j] = 0;
            }
        }
        int m = 0;
        for (String url : webGraph.keySet()) {
            for (String targetUrl : webGraph.get(url)) {
                matrix[urlsToIndices.get(url)][urlsToIndices.get(targetUrl)] = 1;
                m++;
            }
        }

        UrlGraph urlGraph = UrlGraph.builder()
                .n(urls.size())
                .m(m)
                .matrix(matrix)
                .urlsToIndices(urlsToIndices)
                .build();
        cache.put(executionId, urlGraph);
        return urlGraph;
    }

}

