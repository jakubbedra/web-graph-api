package com.konfyrm.webgraphapi.algorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VertexDegreeDistribution {

    private VertexDegreeDistribution() {
        throw new IllegalStateException("Utils class should not be instantiated.");
    }

    public static Map<Integer, Integer> calculateInDegreeDistribution(List<Integer>[] digraph, int n) {
        int[] vertexDegrees = new int[n];
        for (int v = 0; v < n; v++) {
            for (Integer u : digraph[v]) {
                vertexDegrees[u]++;
            }
        }

        return toDistributionsMap(vertexDegrees, n);
    }

    public static Map<Integer, Integer> calculateOutDegreeDistribution(List<Integer>[] digraph, int n) {
        int[] vertexDegrees = new int[n];
        Arrays.fill(vertexDegrees, 0);
        for (int v = 0; v < n; v++) {
            vertexDegrees[v] = digraph[v].size();
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

