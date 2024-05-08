package com.konfyrm.webgraphapi.service;

import com.konfyrm.webgraphapi.domain.model.UrlGraph;

import java.util.Map;
import java.util.Set;

public interface WebGraphService {
    Map<String, Set<String>> getUrlMap(String executionUuid);
    void updateUrlMap(String executionUuid, Map<String, Set<String>> urlMap);
    UrlGraph getOrCreateGraph(String executionId);
}