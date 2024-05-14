package com.konfyrm.webgraphapi.service;

import com.konfyrm.webgraphapi.domain.model.UrlGraph;
import com.konfyrm.webgraphapi.domain.response.ConnectedComponentsResponse;
import com.konfyrm.webgraphapi.domain.response.DisconnectingVerticesResponse;
import com.konfyrm.webgraphapi.domain.response.GraphDistancesResponse;
import com.konfyrm.webgraphapi.domain.response.VertexDegreeDistributionResponse;

public interface GraphAnalysisService {
    GraphDistancesResponse calculateDistances(UrlGraph urlGraph);
    ConnectedComponentsResponse calculateConnectedComponents(UrlGraph urlGraph);
    DisconnectingVerticesResponse findDisconnectingVertices(UrlGraph urlGraph);
    VertexDegreeDistributionResponse calculateVertexDegreeDistribution(UrlGraph urlGraph);
}

