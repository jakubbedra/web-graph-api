package com.konfyrm.webgraphapi.service.impl;

import com.konfyrm.webgraphapi.algorithm.*;
import com.konfyrm.webgraphapi.domain.model.UrlGraph;
import com.konfyrm.webgraphapi.domain.response.*;
import com.konfyrm.webgraphapi.service.GraphAnalysisService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GraphAnalysisServiceImpl implements GraphAnalysisService {

    @Override
    public GraphDistancesResponse calculateDistances(UrlGraph urlGraph) {
//        int[][] distances = FloydWarshallAlgorithm.execute(urlGraph);
        int n = urlGraph.getN();
        int[][] graph = GraphConverter.digraphToAdjacencyMatrixGraph(urlGraph.getNeighbours(), n);
        int[][] distances = FloydWarshallAlgorithm.execute(graph, n);
        int[] eccentricity = new int[n];
        for (int i = 0; i < n; i++) {
            int maxDistance = Integer.MIN_VALUE;
            for (int j = 0; j < n; j++) {
                if (distances[i][j] != Integer.MAX_VALUE && i != j && maxDistance < distances[i][j]) {
                    maxDistance = distances[i][j];
                }
//                if (distances[j][i] != Integer.MAX_VALUE && i != j && maxDistance < distances[j][i]) {
//                    maxDistance = distances[j][i];
//                }
            }
            eccentricity[i] = maxDistance;
        }

        int diameter = 0;
        for (int i = 0; i < n; i++) {
            if (eccentricity[i] != Integer.MIN_VALUE && diameter < eccentricity[i]) {
                diameter = eccentricity[i];
            }
        }
        int radius = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            if (eccentricity[i] != Integer.MIN_VALUE && radius > eccentricity[i]) {
                radius = eccentricity[i];
            }
        }
        double avgDistance = 0.0;
        int distancesCount = 0;
        for (int u = 0; u < n; u++) {
            for (int v = 0; v < n; v++) {
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
    public ConnectedComponentsResponse findConnectedComponents(UrlGraph urlGraph) {
        List<List<Integer>> wcccs = WCCFinder.execute(urlGraph);
        List<List<Integer>> sccs = SCCFinder.execute(urlGraph);
        Map<Integer, List<Integer>> inComponents = InAndOutComponentFinder.findInComponents(urlGraph.getNeighbours(), urlGraph.getN(), sccs);
        Map<Integer, List<Integer>> outComponents = InAndOutComponentFinder.findOutComponents(urlGraph.getNeighbours(), urlGraph.getN(), sccs);
        return ConnectedComponentsResponse.builder()
                .stronglyConnectedComponents(sccs)
                .weaklyConnectedComponents(wcccs)
                .sccCount(sccs.size())
                .wccCount(wcccs.size())
                .inComponents(inComponents)
                .outComponents(outComponents)
                .build();
    }

    @Override
    public DisconnectingVerticesResponse findDisconnectingVertices(UrlGraph urlGraph) {
        int n = urlGraph.getN();
        List<Integer>[] graph = GraphConverter.digraphToGraph(urlGraph.getNeighbours(), n);
        boolean[] visited = new boolean[n];
        Arrays.fill(visited,false);
        List<Integer> bfs = SearchAlgorithms.bfs(graph, 0, visited);
        // disconnected graph
        if (bfs.size() != n) {
            return DisconnectingVerticesResponse.builder()
                    .disconnectingVerticesCount(0)
                    .disconnectingVerticesPairsCount(0)
                    .disconnectingVertices(Collections.emptyList())
                    .disconnectingVerticesPairs(Collections.emptyList())
                    .build();
        }
        List<Integer> disconnectingVertices = DisconnectingVerticesFinder
                .findDisconnectingVertices(graph, n);
        List<Pair<Integer, Integer>> disconnectingVerticesPairs = !disconnectingVertices.isEmpty() ?
                Collections.emptyList() : DisconnectingVerticesFinder
                .findDisconnectingVerticesPairs(graph, n);
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
        double[] localCoefficients = new double[n];
        long globalTriangles = 0;
        long globalTriplets = 0;

        for (int v = 0; v < n; v++) {
            Pair<Integer, Integer> coefficientPair = ClusteringCoefficientCalculator.computeTrianglesAndTriplets(graph, n, v);
            if (coefficientPair != null) {
                localCoefficients[v] = (double) coefficientPair.getFirst() / (double) coefficientPair.getSecond();
                globalTriangles += coefficientPair.getFirst();
                globalTriplets += coefficientPair.getSecond();
            } else {
                localCoefficients[v] = 0.0;
            }
        }

        globalTriplets /= 2;
        double globalCoefficient = (double) globalTriangles / (double) globalTriplets;
        return ClusteringCoefficientsResponse.builder()
                .global(globalCoefficient)
                .local(localCoefficients)
                .build();
    }

    @Override
    public PageRankResponse calculatePageRank(UrlGraph urlGraph, double dampingFactor) {
        int n = urlGraph.getN();
        Pair<Double[], Integer> pageRank = PageRank.execute(urlGraph.getNeighbours(), n, dampingFactor);
        return buildPageRankResponse(n, pageRank);
    }

    @Override
    public PageRankResponse calculatePageRank(UrlGraph urlGraph) {
        int n = urlGraph.getN();
        Pair<Double[], Integer> pageRank = PageRank.execute(urlGraph.getNeighbours(), n);
        return buildPageRankResponse(n, pageRank);
    }

    private PageRankResponse buildPageRankResponse(int n, Pair<Double[], Integer> pageRank) {
        Map<Double, Integer> distribution = AlgorithmUtils.valuesToDistribution(pageRank.getFirst(), n);
        Pair<Double, Double> coefficients = PowerFunctionFitting.fitPowerFunctionDouble(distribution);

        return PageRankResponse.builder()
                .pageRank(pageRank.getFirst())
                .iterationsCount(pageRank.getSecond())
                .distribution(distribution)
                .a(coefficients.getFirst())
                .b(coefficients.getSecond())
                .build();
    }

}
