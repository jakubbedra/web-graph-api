package com.konfyrm.webgraphapi.algorithm;

import com.konfyrm.webgraphapi.domain.model.UrlGraph;

public class FloydWarshallAlgorithm {

    private FloydWarshallAlgorithm() {
        throw new IllegalStateException("Utils class should not be instantiated.");
    }

    public static int[][] execute(UrlGraph graph) {
        int[][] shortestPaths = new int[graph.getN()][graph.getN()];
        for (int i = 0; i < shortestPaths.length; i++) {
            for (int j = 0; j < shortestPaths.length; j++) {
                shortestPaths[i][j] = Integer.MAX_VALUE;
            }
        }
        for (int v = 0; v < graph.getN(); v++) {
            for (Integer u : graph.getNeighbours()[v]) {
                shortestPaths[v][u] = 1;
            }
        }
        for (int i = 0; i < graph.getN(); i++) {
            shortestPaths[i][i] = 0;
        }
        for (int k = 0; k < graph.getN(); k++) {
            for (int i = 0; i < graph.getN(); i++) {
                for (int j = 0; j < graph.getN(); j++) {
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