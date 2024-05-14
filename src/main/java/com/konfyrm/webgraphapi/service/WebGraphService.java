package com.konfyrm.webgraphapi.service;

import com.konfyrm.webgraphapi.domain.message.UrlNode;
import com.konfyrm.webgraphapi.domain.model.UrlGraph;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface WebGraphService {
    List<UrlNode> getUrlMap(String executionUuid);
    void updateUrlMap(String executionUuid, List<UrlNode> urlNodes);
    UrlGraph getOrCreateGraph(String executionId);
    void exportJson(String executionId);
    void importJson(String executionId);
}