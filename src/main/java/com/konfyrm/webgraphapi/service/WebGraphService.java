package com.konfyrm.webgraphapi.service;

import com.konfyrm.webgraphapi.domain.message.UrlNode;
import com.konfyrm.webgraphapi.domain.model.UrlGraph;

import java.util.List;

public interface WebGraphService {
    List<UrlNode> getUrlMap(String executionUuid);
    void updateUrlMap(String executionUuid, List<UrlNode> urlNodes);
    UrlGraph getOrCreateGraph(String executionId);
    UrlGraph getWithDeletedMaxDegreeVertices(String executionId, int amount, boolean outDegree);
    UrlGraph getWithDeletedRandomVertices(String executionId, int amount);
    void exportJson(String executionId);
    void importJson(String executionId);
}