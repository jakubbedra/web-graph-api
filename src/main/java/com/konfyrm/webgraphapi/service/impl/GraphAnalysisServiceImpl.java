package com.konfyrm.webgraphapi.service.impl;

import com.konfyrm.webgraphapi.algorithm.*;
import com.konfyrm.webgraphapi.domain.model.UrlGraph;
import com.konfyrm.webgraphapi.domain.response.*;
import com.konfyrm.webgraphapi.service.GraphAnalysisService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class GraphAnalysisServiceImpl implements GraphAnalysisService {

    @Override
    public GraphDistancesResponse calculateDistances(UrlGraph urlGraph) {
        int[][] distances = FloydWarshallAlgorithm.execute(urlGraph);
        int[] eccentricity = new int[urlGraph.getN()];
        for (int i = 0; i < urlGraph.getN(); i++) {
            int maxDistance = 0;
            for (int j = 0; j < urlGraph.getN(); j++) {
                if (distances[i][j] != Integer.MAX_VALUE && i != j && maxDistance < distances[i][j]) {
                    maxDistance = distances[i][j];
                }
            }
            eccentricity[i] = maxDistance;
        }

        int diameter = 0;
        for (int i = 0; i < urlGraph.getN(); i++) {
            if (diameter < eccentricity[i]) {
                diameter = eccentricity[i];
            }
        }
        int radius = 0;
        for (int i = 0; i < urlGraph.getN(); i++) {
            if (radius > eccentricity[i]) {
                radius = eccentricity[i];
            }
        }
        double avgDistance = 0.0;
        int distancesCount = 0;
        for (int u = 0; u < urlGraph.getN(); u++) {
            for (int v = 0; v < urlGraph.getN(); v++) {
                if (u != v && distances[u][v] != Integer.MAX_VALUE) {
                    avgDistance += distances[u][v];
                    distancesCount++;
                }
            }
        }
        avgDistance /= distancesCount;
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
        List<Pair<Integer, Integer>> disconnectingVerticesPairs = !disconnectingVertices.isEmpty() ?
                Collections.emptyList() : DisconnectingVerticesFinder
                .findDisconnectingVerticesPairs(urlGraph.getNeighbours(), urlGraph.getN());
        return DisconnectingVerticesResponse.builder()
                .disconnectingVerticesCount(disconnectingVertices.size())
                .disconnectingVerticesPairsCount(disconnectingVerticesPairs.size())
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
        Pair<Double, Double> inCoefficients = PowerFunctionFitting.fitPowerFunction(inDegrees);
        Pair<Double, Double> outCoefficients = PowerFunctionFitting.fitPowerFunction(outDegrees);
        return VertexDegreeDistributionResponse.builder()
                .inDegreeDistribution(inDegrees)
                .outDegreeDistribution(outDegrees)
                .aIn(inCoefficients.getFirst())
                .bIn(inCoefficients.getSecond())
                .aOut(outCoefficients.getFirst())
                .bOut(outCoefficients.getSecond())
                .build();
    }

    @Override
    public ClusteringCoefficientsResponse calculateClusteringCoefficients(UrlGraph urlGraph) {
        int n = urlGraph.getN();
        int[][] graph = GraphConverter.digraphToAdjacencyMatrixGraph(urlGraph.getNeighbours(), n);
        double globalCoefficient = ClusteringCoefficientCalculator.computeGlobal(graph, n);
        double[] localCoefficients = new double[n];

        for (int v = 0; v < n; v++) {
            localCoefficients[v] = ClusteringCoefficientCalculator.computeLocal(graph, n, v);
        }

        return ClusteringCoefficientsResponse.builder()
                .global(globalCoefficient)
                .local(localCoefficients)
                .build();
    }

    @Override
    public PageRankResponse calculatePageRank(UrlGraph urlGraph, double dampingFactor) {
        int n = urlGraph.getN();
        Double[] pageRank = PageRank.execute(urlGraph.getNeighbours(), n, dampingFactor);
        Map<Double, Integer> distribution = AlgorithmUtils.valuesToDistribution(pageRank, n);
        return PageRankResponse.builder()
                .pageRank(pageRank)
                .distribution(distribution)
                .build();
    }

}
