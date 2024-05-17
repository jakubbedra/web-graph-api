package com.konfyrm.webgraphapi.controller;

import com.konfyrm.webgraphapi.domain.request.PageRankRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/graphs/{executionUuid}")
public interface WebGraphController {

    @GetMapping
    ResponseEntity<?> getGraph(@PathVariable("executionUuid") String executionUuid);

    @PostMapping("/distances")
    ResponseEntity<?> calculateDistances(@PathVariable("executionUuid") String executionUuid);

    @PostMapping("/components")
    ResponseEntity<?> findConnectedComponents(@PathVariable("executionUuid") String executionUuid);

    @PostMapping("/disconnecting-vertices")
    ResponseEntity<?> findDisconnectingVertices(@PathVariable("executionUuid") String executionUuid);

    @PostMapping("/distribution")
    ResponseEntity<?> calculateVertexDegreeDistribution(@PathVariable("executionUuid") String executionUuid);

    @PostMapping("/clustering-coefficients")
    ResponseEntity<?> calculateClusteringCoefficients(@PathVariable("executionUuid") String executionUuid);

    @PostMapping("/page-rank")
    ResponseEntity<?> calculatePageRank(@PathVariable("executionUuid") String executionUuid, @RequestBody PageRankRequest request);

    // exports and import from json file

    @PostMapping("/export")
    ResponseEntity<?> exportGraph(@PathVariable("executionUuid") String executionUuid);

    @PostMapping("/import")
    ResponseEntity<?> importGraph(@PathVariable("executionUuid") String executionUuid);

}
