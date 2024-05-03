package com.konfyrm.webgraphapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("api/v1/graphs")
public interface WebGraphController {

    @GetMapping("/{executionUuid}")
    ResponseEntity<?> getGraph(@PathVariable("executionUuid") String executionUuid);

}