package com.konfyrm.webgraphapi.service;

public interface UrlVisitService {
    void sendUrlVisitRequest(String executionUuid, String startingUrl);
}
