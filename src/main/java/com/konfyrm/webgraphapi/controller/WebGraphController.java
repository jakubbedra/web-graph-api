package com.konfyrm.webgraphapi.controller;

import com.konfyrm.webgraphapi.domain.request.PageRankRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/graphs")
public interface WebGraphController {

    @GetMapping("/{executionUuid}")
    ResponseEntity<?> getGraph(@PathVariable("executionUuid") String executionUuid);

    @PostMapping("/{executionUuid}/distances")
    ResponseEntity<?> calculateDistances(@PathVariable("executionUuid") String executionUuid);

    @PostMapping("/{executionUuid}/components")
    ResponseEntity<?> findConnectedComponents(@PathVariable("executionUuid") String executionUuid);

    @PostMapping("/{executionUuid}/disconnecting-vertices")
    ResponseEntity<?> findDisconnectingVertices(@PathVariable("executionUuid") String executionUuid);

    @PostMapping("/{executionUuid}/distribution")
    ResponseEntity<?> calculateVertexDegreeDistribution(@PathVariable("executionUuid") String executionUuid);

    @PostMapping("/{executionUuid}/clustering-coefficients")
    ResponseEntity<?> calculateClusteringCoefficients(@PathVariable("executionUuid") String executionUuid);

    @PostMapping("/{executionUuid}/page-rank")
    ResponseEntity<?> calculatePageRank(@PathVariable("executionUuid") String executionUuid, @RequestBody PageRankRequest request);

    // exports and import from json file

    @PostMapping("/{executionUuid}/export")
    ResponseEntity<?> exportGraph(@PathVariable("executionUuid") String executionUuid);

    @PostMapping("/{executionUuid}/import")
    ResponseEntity<?> importGraph(@PathVariable("executionUuid") String executionUuid);

}