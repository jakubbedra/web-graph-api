package com.konfyrm.webgraphapi.controller.impl;

import com.konfyrm.webgraphapi.controller.WebGraphController;
import com.konfyrm.webgraphapi.domain.entity.Execution;
import com.konfyrm.webgraphapi.domain.model.UrlGraph;
import com.konfyrm.webgraphapi.domain.request.PageRankRequest;
import com.konfyrm.webgraphapi.domain.response.*;
import com.konfyrm.webgraphapi.maanger.JsonFileManager;
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
    private final JsonFileManager jsonFileManager;

    @Autowired
    public WebGraphControllerImpl(
            @Qualifier("executionServiceImpl") ExecutionService executionService,
            @Qualifier("webGraphServiceImpl") WebGraphService webGraphService,
            @Qualifier("graphAnalysisServiceImpl") GraphAnalysisService graphAnalysisService,
            @Qualifier("jsonFileManagerImpl") JsonFileManager jsonFileManager
    ) {
        this.executionService = executionService;
        this.webGraphService = webGraphService;
        this.graphAnalysisService = graphAnalysisService;
        this.jsonFileManager = jsonFileManager;
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
    public ResponseEntity<?> calculateDistances(String executionUuid) {
//        Optional<Execution> executionOptional = executionService.findByUuid(executionUuid);
//        if (executionOptional.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
        // todo: check if execution finished
        UrlGraph graph = webGraphService.getOrCreateGraph(executionUuid);
        GraphDistancesResponse graphDistancesResponse = graphAnalysisService.calculateDistances(graph);
        jsonFileManager.exportObject(graphDistancesResponse, executionUuid);
        return ResponseEntity.ok(graphDistancesResponse);
    }

    //todo: POST endpoints should only export to file?
    @Override
    public ResponseEntity<?> findConnectedComponents(String executionUuid) {
//        Optional<Execution> executionOptional = executionService.findByUuid(executionUuid);
//        if (executionOptional.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
        UrlGraph graph = webGraphService.getOrCreateGraph(executionUuid);
        ConnectedComponentsResponse response = graphAnalysisService.findConnectedComponents(graph);
        jsonFileManager.exportObject(response, executionUuid);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> findDisconnectingVertices(String executionUuid) {
//        Optional<Execution> executionOptional = executionService.findByUuid(executionUuid);
//        if (executionOptional.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
        UrlGraph graph = webGraphService.getOrCreateGraph(executionUuid);
        DisconnectingVerticesResponse response = graphAnalysisService.findDisconnectingVertices(graph);
        jsonFileManager.exportObject(response, executionUuid);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> calculateVertexDegreeDistribution(String executionUuid) {
//        Optional<Execution> executionOptional = executionService.findByUuid(executionUuid);
//        if (executionOptional.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
        UrlGraph graph = webGraphService.getOrCreateGraph(executionUuid);
        VertexDegreeDistributionResponse response = graphAnalysisService.calculateVertexDegreeDistribution(graph);
        jsonFileManager.exportObject(response, executionUuid);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> calculateClusteringCoefficients(String executionUuid) {
        UrlGraph graph = webGraphService.getOrCreateGraph(executionUuid);
        ClusteringCoefficientsResponse response = graphAnalysisService.calculateClusteringCoefficients(graph);
        jsonFileManager.exportObject(response, executionUuid);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> calculatePageRank(String executionUuid, PageRankRequest request) {
        UrlGraph graph = webGraphService.getOrCreateGraph(executionUuid);
        PageRankResponse response = request.isWithDamping() ?
                graphAnalysisService.calculatePageRank(graph, request.getDampingFactor()) :
                graphAnalysisService.calculatePageRank(graph);
        jsonFileManager.exportObject(response, "[" + (request.isWithDamping() ? request.getDampingFactor() : "-") + "]" + executionUuid);
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

