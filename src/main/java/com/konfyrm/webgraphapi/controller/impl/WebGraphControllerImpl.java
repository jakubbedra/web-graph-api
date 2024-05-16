package com.konfyrm.webgraphapi.controller.impl;

import com.konfyrm.webgraphapi.controller.WebGraphController;
import com.konfyrm.webgraphapi.domain.entity.Execution;
import com.konfyrm.webgraphapi.domain.model.UrlGraph;
import com.konfyrm.webgraphapi.domain.response.ConnectedComponentsResponse;
import com.konfyrm.webgraphapi.domain.response.DisconnectingVerticesResponse;
import com.konfyrm.webgraphapi.domain.response.GraphDistancesResponse;
import com.konfyrm.webgraphapi.domain.response.VertexDegreeDistributionResponse;
import com.konfyrm.webgraphapi.service.ExecutionService;
import com.konfyrm.webgraphapi.service.GraphAnalysisService;
import com.konfyrm.webgraphapi.service.WebGraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class WebGraphControllerImpl implements WebGraphController {

    private final ExecutionService executionService;
    private final WebGraphService webGraphService;
    private final GraphAnalysisService graphAnalysisService;

    @Autowired
    public WebGraphControllerImpl(
            @Qualifier("executionServiceImpl") ExecutionService executionService,
            @Qualifier("webGraphServiceImpl") WebGraphService webGraphService,
            @Qualifier("graphAnalysisServiceImpl") GraphAnalysisService graphAnalysisService
    ) {
        this.executionService = executionService;
        this.webGraphService = webGraphService;
        this.graphAnalysisService = graphAnalysisService;
    }

    @Override
    public ResponseEntity<?> getGraph(String executionUuid) {
        Optional<Execution> executionOptional = executionService.findByUuid(executionUuid);
        if (executionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Execution execution = executionOptional.get();
        if (execution.getTasksInProgress() != 0) {
            return ResponseEntity.notFound().build();
        }

        UrlGraph graph = webGraphService.getOrCreateGraph(executionUuid);
        return ResponseEntity.ok().build(); // todo: a dto maybe?
    }

    @Override
    public ResponseEntity<?> getDistances(String executionUuid) {
//        Optional<Execution> executionOptional = executionService.findByUuid(executionUuid);
//        if (executionOptional.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
        // todo: check if execution finished
        UrlGraph graph = webGraphService.getOrCreateGraph(executionUuid);
        GraphDistancesResponse graphDistancesResponse = graphAnalysisService.calculateDistances(graph);
        return ResponseEntity.ok(graphDistancesResponse);
    }

    @Override
    public ResponseEntity<?> getConnectedComponents(String executionUuid) {
//        Optional<Execution> executionOptional = executionService.findByUuid(executionUuid);
//        if (executionOptional.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
        UrlGraph graph = webGraphService.getOrCreateGraph(executionUuid);
        ConnectedComponentsResponse response = graphAnalysisService.calculateConnectedComponents(graph);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getDisconnectingVertices(String executionUuid) {
//        Optional<Execution> executionOptional = executionService.findByUuid(executionUuid);
//        if (executionOptional.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
        UrlGraph graph = webGraphService.getOrCreateGraph(executionUuid);
        DisconnectingVerticesResponse response = graphAnalysisService.findDisconnectingVertices(graph);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getVertexDegreeDistribution(String executionUuid) {
//        Optional<Execution> executionOptional = executionService.findByUuid(executionUuid);
//        if (executionOptional.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
        UrlGraph graph = webGraphService.getOrCreateGraph(executionUuid);
        VertexDegreeDistributionResponse response = graphAnalysisService.calculateVertexDegreeDistribution(graph);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> exportGraph(String executionUuid) {
        webGraphService.exportJson(executionUuid);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> importGraph(String executionUuid) {
        webGraphService.importJson(executionUuid);
        return ResponseEntity.ok().build();
    }

}
