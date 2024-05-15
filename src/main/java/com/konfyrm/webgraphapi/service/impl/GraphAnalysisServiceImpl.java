package com.konfyrm.webgraphapi.service.impl;

import com.konfyrm.webgraphapi.algorithm.*;
import com.konfyrm.webgraphapi.domain.model.UrlGraph;
import com.konfyrm.webgraphapi.domain.response.ConnectedComponentsResponse;
import com.konfyrm.webgraphapi.domain.response.DisconnectingVerticesResponse;
import com.konfyrm.webgraphapi.domain.response.GraphDistancesResponse;
import com.konfyrm.webgraphapi.domain.response.VertexDegreeDistributionResponse;
import com.konfyrm.webgraphapi.service.GraphAnalysisService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GraphAnalysisServiceImpl implements GraphAnalysisService {

    @Override
    public GraphDistancesResponse calculateDistances(UrlGraph urlGraph) {
        int[][] distances = FloydWarshallAlgorithm.execute(urlGraph);
        int[] eccentricity = new int[urlGraph.getN()];
        for (int i = 0; i < urlGraph.getN(); i++) {
            int maxDistance = distances[i][0];
            for (int j = 1; j < urlGraph.getN(); j++) {
                if (maxDistance < distances[i][j]) {
                    maxDistance = distances[i][j];
                }
            }
            eccentricity[i] = maxDistance;
        }

        int diameter = eccentricity[0];
        for (int i = 0; i < urlGraph.getN(); i++) {
            if (diameter < eccentricity[i]) {
                diameter = eccentricity[i];
            }
        }
        int radius = eccentricity[0];
        for (int i = 0; i < urlGraph.getN(); i++) {
            if (radius > eccentricity[i]) {
                radius = eccentricity[i];
            }
        }
        double avgDistance = 0.0;
        for (int u = 0; u < urlGraph.getN(); u++) {
            for (int v = 0; v <urlGraph.getN(); v++) {
                if (distances[u][v] != Integer.MAX_VALUE) {
                    avgDistance += distances[u][v];
                }
            }
        }
        return GraphDistancesResponse.builder()
                .diameter(diameter)
                .radius(radius)
                .distances(distances)
                .avgDistance(avgDistance)
                .build();
    }

    @Override
    public ConnectedComponentsResponse calculateConnectedComponents(UrlGraph urlGraph) {
        List<List<Integer>> wcccs = WCCFinder.execute(urlGraph);
        List<List<Integer>> sccs = SCCFinder.execute(urlGraph);
        return ConnectedComponentsResponse.builder()
                .stronglyConnectedComponents(sccs)
                .weaklyConnectedComponents(wcccs)
                .sccCount(sccs.size())
                .wccCount(wcccs.size())
                .build();
    }

    @Override
    public DisconnectingVerticesResponse findDisconnectingVertices(UrlGraph urlGraph) {
        List<Integer> disconnectingVertices = DisconnectingVerticesFinder
                .findDisconnectingVertices(urlGraph.getNeighbours(), urlGraph.getN());
        List<Pair<Integer, Integer>> disconnectingVerticesPairs = DisconnectingVerticesFinder
                .findDisconnectingVerticesPairs(urlGraph.getNeighbours(), urlGraph.getN());
        return DisconnectingVerticesResponse.builder()
                .disconnectingVertices(disconnectingVertices)
                .disconnectingVerticesPairs(disconnectingVerticesPairs)
                .build();
    }

    @Override
    public VertexDegreeDistributionResponse calculateVertexDegreeDistribution(UrlGraph urlGraph) {
        Map<Integer, Integer> inDegrees = VertexDegreeDistribution
                .calculateInDegreeDistribution(urlGraph.getNeighbours(), urlGraph.getN());
        Map<Integer, Integer> outDegrees = VertexDegreeDistribution
                .calculateOutDegreeDistribution(urlGraph.getNeighbours(), urlGraph.getN());
        // todo: power function coefficient
        return VertexDegreeDistributionResponse.builder()
                .inDegreeDistribution(inDegrees)
                .outDegreeDistribution(outDegrees)
                .build();
    }

}

