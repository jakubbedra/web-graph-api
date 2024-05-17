package com.konfyrm.webgraphapi.algorithm;

import com.konfyrm.webgraphapi.domain.model.UrlGraph;

public class FloydWarshallAlgorithm {

    private FloydWarshallAlgorithm() {
        throw new IllegalStateException("Utils class should not be instantiated.");
    }

    public static int[][] execute(UrlGraph graph) {
        int n = graph.getN();
        int[][] shortestPaths = new int[n][n];
        for (int i = 0; i < shortestPaths.length; i++) {
            for (int j = 0; j < shortestPaths.length; j++) {
                shortestPaths[i][j] = Integer.MAX_VALUE;
            }
        }
        for (int v = 0; v < n; v++) {
            for (Integer u : graph.getNeighbours()[v]) {
                shortestPaths[v][u] = 1;
            }
        }
        for (int i = 0; i < n; i++) {
            shortestPaths[i][i] = 0;
        }
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (shortestPaths[i][k] != Integer.MAX_VALUE && shortestPaths[k][j] != Integer.MAX_VALUE &&
                            shortestPaths[i][j] > shortestPaths[i][k] + shortestPaths[k][j]) {
                        shortestPaths[i][j] = shortestPaths[i][k] + shortestPaths[k][j];
                    }
                }
            }
        }
        return shortestPaths;
    }

}