package com.konfyrm.webgraphapi.controller.impl;

import com.konfyrm.webgraphapi.controller.ExecutionController;
import com.konfyrm.webgraphapi.domain.entity.Execution;
import com.konfyrm.webgraphapi.domain.request.ExecutionRequest;
import com.konfyrm.webgraphapi.service.ExecutionService;
import com.konfyrm.webgraphapi.service.UrlVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExecutionControllerImpl implements ExecutionController {

    @Value("${application.api.max-visited-nodes-per-request}")
    private int maxVisitedNodesPerRequest;

    private final UrlVisitService urlVisitService;
    private final ExecutionService executionService;

    @Autowired
    public ExecutionControllerImpl(
            @Qualifier("urlVisitServiceImpl") UrlVisitService urlVisitService,
            @Qualifier("executionServiceImpl") ExecutionService executionService
    ) {
        this.urlVisitService = urlVisitService;
        this.executionService = executionService;
    }

    @Override
    public ResponseEntity<?> sendUrlVisitRequest(ExecutionRequest request) {
        Execution execution = Execution.builder()
                .tasksInProgress(1)
                .build();
        String executionUuid = executionService.save(execution);
        urlVisitService.sendUrlVisitRequest(executionUuid, request.getUrl(), maxVisitedNodesPerRequest);
        return ResponseEntity.ok(executionUuid);
    }

}
