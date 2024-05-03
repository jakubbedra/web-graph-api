package com.konfyrm.webgraphapi.controller;

import com.konfyrm.webgraphapi.domain.request.ExecutionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("api/v1/executions")
public interface ExecutionController {
    @PostMapping
    ResponseEntity<?> sendUrlVisitRequest(@RequestBody ExecutionRequest request);
}
