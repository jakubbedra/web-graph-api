package com.konfyrm.webgraphapi.controller.impl;

import com.konfyrm.webgraphapi.controller.ExecutionController;
import com.konfyrm.webgraphapi.domain.entity.Execution;
import com.konfyrm.webgraphapi.domain.request.ExecutionRequest;
import com.konfyrm.webgraphapi.domain.response.ExecutionResponse;
import com.konfyrm.webgraphapi.service.ExecutionService;
import com.konfyrm.webgraphapi.service.UrlVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ExecutionControllerImpl implements ExecutionController {

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
    public ResponseEntity<?> getExecution(String executionUuid) {
        Optional<Execution> executionOptional = executionService.findByUuid(executionUuid);
        if (executionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Execution execution = executionOptional.get();
        ExecutionResponse response = ExecutionResponse.builder()
                .lastUpdateTimestamp(execution.getLastUpdateTimestamp())
                .triggeredTimestamp(execution.getTriggeredTimestamp())
                .tasksInProgress(execution.getTasksInProgress())
                .build();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> sendUrlVisitRequest(ExecutionRequest request) {
        Execution execution = Execution.builder()
                .tasksInProgress(1)
                .downloadFiles(request.isDownloadFiles())
                .build();
        String executionUuid = executionService.save(execution);
        urlVisitService.sendUrlVisitRequest(executionUuid, request.getUrl(), request.isDownloadFiles());
        return ResponseEntity.ok(executionUuid);
    }

}