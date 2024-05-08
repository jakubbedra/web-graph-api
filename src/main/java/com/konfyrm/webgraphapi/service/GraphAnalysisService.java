package com.konfyrm.webgraphapi.service;

import com.konfyrm.webgraphapi.domain.model.UrlGraph;
import com.konfyrm.webgraphapi.domain.response.GraphDistancesResponse;

public interface GraphAnalysisService {
    GraphDistancesResponse calculateDistances(UrlGraph urlGraph);
}

