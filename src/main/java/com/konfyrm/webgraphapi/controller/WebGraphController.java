package com.konfyrm.webgraphapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("api/v1/graphs")
public interface WebGraphController {

    @GetMapping("/{executionUuid}")
    ResponseEntity<?> getGraph(@PathVariable("executionUuid") String executionUuid);

    @GetMapping("/{executionUuid}/distances")
    ResponseEntity<?> getDistances(@PathVariable("executionUuid") String executionUuid);

    @GetMapping("/{executionUuid}/components")
    ResponseEntity<?> getConnectedComponents(@PathVariable("executionUuid") String executionUuid);

    @GetMapping("/{executionUuid}/disconnecting-vertices")
    ResponseEntity<?> getDisconnectingVertices(@PathVariable("executionUuid") String executionUuid);

    @GetMapping("/{executionUuid}/distribution")
    ResponseEntity<?> getVertexDegreeDistribution(@PathVariable("executionUuid") String executionUuid);

    @GetMapping("/{executionUuid}/clustering-coefficients")
    ResponseEntity<?> getClusteringCoefficients(@PathVariable("executionUuid") String executionUuid);

    @GetMapping("/{executionUuid}/page-rank")
    ResponseEntity<?> getPageRank(@PathVariable("executionUuid") String executionUuid);

    // exports and import from json file

    @PostMapping("/{executionUuid}/export")
    ResponseEntity<?> exportGraph(@PathVariable("executionUuid") String executionUuid);

    @PostMapping("/{executionUuid}/import")
    ResponseEntity<?> importGraph(@PathVariable("executionUuid") String executionUuid);

}