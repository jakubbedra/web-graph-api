package com.konfyrm.webgraphapi.algorithm;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class VertexDegreeDistribution {

    private VertexDegreeDistribution() {
        throw new IllegalStateException("Utils class should not be instantiated.");
    }

    public static Map<Integer, Integer> calculateInDegreeDistribution(List<Integer>[] digraph, int n) {
        Integer[] vertexDegrees = new Integer[n];
        Arrays.fill(vertexDegrees, 0);
        for (int v = 0; v < n; v++) {
            for (Integer u : digraph[v]) {
                vertexDegrees[u]++;
            }
        }

        return AlgorithmUtils.valuesToDistribution(vertexDegrees, n);
    }

    public static Map<Integer, Integer> calculateOutDegreeDistribution(List<Integer>[] digraph, int n) {
        Integer[] vertexDegrees = new Integer[n];
        Arrays.fill(vertexDegrees, 0);
        for (int v = 0; v < n; v++) {
            vertexDegrees[v] = digraph[v].size();
        }

        return AlgorithmUtils.valuesToDistribution(vertexDegrees, n);
    }

}

