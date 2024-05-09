package com.konfyrm.webgraphapi.algorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class VertexDegreeDistribution {

    private VertexDegreeDistribution() {}

    public static Map<Integer, Integer> calculateInDegreeDistribution(int[][] digraph, int n) {
        int[] vertexDegrees = new int[n];
        Arrays.fill(vertexDegrees, 0);
        for (int v = 0; v < n; v++) {
            for (int u = 0; u < n; u++) {
                vertexDegrees[v] += digraph[u][v];
            }
        }

        return toDistributionsMap(vertexDegrees, n);
    }

    public static Map<Integer, Integer> calculateOutDegreeDistribution(int[][] digraph, int n) {
        int[] vertexDegrees = new int[n];
        Arrays.fill(vertexDegrees, 0);
        for (int v = 0; v < n; v++) {
            for (int u = 0; u < n; u++) {
                vertexDegrees[u] += digraph[v][u];
            }
        }

        return toDistributionsMap(vertexDegrees, n);
    }

    private static Map<Integer, Integer> toDistributionsMap(int[] vertexDegrees, int n) {
        Map<Integer, Integer> distributions = new HashMap<>();
        for (int v = 0; v < n; v++) {
            int deg = vertexDegrees[v];
            int distibution = distributions.getOrDefault(deg, 0);
            distibution++;
            distributions.put(deg, distibution);
        }
        return distributions;
    }

}

