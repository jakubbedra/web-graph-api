package com.konfyrm.webgraphapi.controller;

import com.konfyrm.webgraphapi.domain.request.ExecutionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/executions")
public interface ExecutionController {

    @GetMapping("/{executionUuid}")
    ResponseEntity<?> getExecution(@PathVariable("executionUuid") String executionUuid);

    @PostMapping
    ResponseEntity<?> sendUrlVisitRequest(@RequestBody ExecutionRequest request);

}