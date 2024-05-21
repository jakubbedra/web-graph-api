package com.konfyrm.webgraphapi.controller.impl;

import com.konfyrm.webgraphapi.controller.WebGraphSimulationController;
import com.konfyrm.webgraphapi.domain.model.UrlGraph;
import com.konfyrm.webgraphapi.domain.request.SimulateAttackRequest;
import com.konfyrm.webgraphapi.domain.request.SimulateMalfunctionRequest;
import com.konfyrm.webgraphapi.domain.response.*;
import com.konfyrm.webgraphapi.maanger.JsonFileManager;
import com.konfyrm.webgraphapi.service.GraphAnalysisService;
import com.konfyrm.webgraphapi.service.WebGraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class WebGraphSimulationControllerImpl implements WebGraphSimulationController {

    private final WebGraphService webGraphService;
    private final GraphAnalysisService graphAnalysisService;
    private final JsonFileManager jsonFileManager;

    @Autowired
    public WebGraphSimulationControllerImpl(
            @Qualifier("webGraphServiceImpl") WebGraphService webGraphService,
            @Qualifier("graphAnalysisServiceImpl") GraphAnalysisService graphAnalysisService,
            @Qualifier("jsonFileManagerImpl") JsonFileManager jsonFileManager
    ) {
        this.webGraphService = webGraphService;
        this.graphAnalysisService = graphAnalysisService;
        this.jsonFileManager = jsonFileManager;
    }

    @Override
    public ResponseEntity<?> simulateAttack(String executionUuid, SimulateAttackRequest request) {
        int verticesToDelete = request.getTopDegreeVerticesCount();
        UrlGraph urlGraph = webGraphService.getWithDeletedMaxDegreeVertices(executionUuid,  verticesToDelete, request.getType().equals("out"));
        ConnectedComponentsResponse connectedComponents = graphAnalysisService.findConnectedComponents(urlGraph);
        VertexDegreeDistributionResponse degreeDistribution = graphAnalysisService.calculateVertexDegreeDistribution(urlGraph);
        GraphDistancesResponse graphDistances = graphAnalysisService.calculateDistances(urlGraph);
        DisconnectingVerticesResponse disconnectingVertices = graphAnalysisService.findDisconnectingVertices(urlGraph);
        UrlGraph graph = webGraphService.getOrCreateGraph(executionUuid);
        HashMap<String, Integer> removedVertices = new HashMap<>(graph.getUrlsToIndices());
        urlGraph.getUrlsToIndices().keySet().forEach(removedVertices::remove);
        GraphDataResponse response = GraphDataResponse.builder()
                .connectedComponents(connectedComponents)
                .degreeDistribution(degreeDistribution)
                .distances(graphDistances)
                .disconnectingVertices(disconnectingVertices)
                .removedVertices(removedVertices)
                .m(urlGraph.getM())
                .build();

        jsonFileManager.exportObject(response, "attacks/[" + verticesToDelete + "-" + request.getType() + "]" + executionUuid);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> simulateMalfunction(String executionUuid, SimulateMalfunctionRequest request) {
        ExecutorService executorService = Executors.newFixedThreadPool(Math.min(10, request.getRepetitions()));
        List<CompletableFuture<Void>> futures = new ArrayList<>(request.getRepetitions());
        for (int i = 0; i < request.getRepetitions(); i++) {
            int finalI = i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(
                    () -> processMalfunction(executionUuid, request, finalI),
                    executorService
            );
            futures.add(future);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return ResponseEntity.ok().build();
    }

    private void processMalfunction(String executionUuid, SimulateMalfunctionRequest request, int i) {
        int verticesToDelete = request.getRandomVerticesCount();
        UrlGraph urlGraph = webGraphService.getWithDeletedRandomVertices(executionUuid,  verticesToDelete);
        ConnectedComponentsResponse connectedComponents = graphAnalysisService.findConnectedComponents(urlGraph);
        VertexDegreeDistributionResponse degreeDistribution = graphAnalysisService.calculateVertexDegreeDistribution(urlGraph);
        GraphDistancesResponse graphDistances = graphAnalysisService.calculateDistances(urlGraph);
        DisconnectingVerticesResponse disconnectingVertices = graphAnalysisService.findDisconnectingVertices(urlGraph);
        UrlGraph graph = webGraphService.getOrCreateGraph(executionUuid);
        HashMap<String, Integer> removedVertices = new HashMap<>(graph.getUrlsToIndices());
        urlGraph.getUrlsToIndices().keySet().forEach(removedVertices::remove);
        GraphDataResponse response = GraphDataResponse.builder()
                .connectedComponents(connectedComponents)
                .degreeDistribution(degreeDistribution)
                .distances(graphDistances)
                .disconnectingVertices(disconnectingVertices)
                .removedVertices(removedVertices)
                .m(urlGraph.getM())
                .build();

        jsonFileManager.exportObject(response, "malfunctions/[" + verticesToDelete + "-" + (i+4) + "]" + executionUuid);
    }

}
