package com.konfyrm.webgraphapi.service;

import com.konfyrm.webgraphapi.domain.model.UrlGraph;
import com.konfyrm.webgraphapi.domain.response.*;

public interface GraphAnalysisService {
    GraphDistancesResponse calculateDistances(UrlGraph urlGraph);
    ConnectedComponentsResponse calculateConnectedComponents(UrlGraph urlGraph);
    DisconnectingVerticesResponse findDisconnectingVertices(UrlGraph urlGraph);
    VertexDegreeDistributionResponse calculateVertexDegreeDistribution(UrlGraph urlGraph);
    ClusteringCoefficientsResponse calculateClusteringCoefficients(UrlGraph urlGraph);
    PageRankResponse calculatePageRank(UrlGraph urlGraph, double dampingFactor);
}
