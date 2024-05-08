package com.konfyrm.webgraphapi.algorithm;

import com.konfyrm.webgraphapi.domain.model.UrlGraph;

public class FloydWarshallAlgorithm {

    public static int[][] execute(UrlGraph graph) {
        int[][] shortestPaths = new int[graph.getN()][graph.getN()];
        for (int i = 0; i < shortestPaths.length; i++) {
            for (int j = 0; j < shortestPaths.length; j++) {
                shortestPaths[i][j] = Integer.MAX_VALUE;
            }
        }
        for (int i = 0; i < graph.getN(); i++) {
            for (int j = 0; j < graph.getN(); j++) {
                if (graph.getMatrix()[i][j] > 0){
                    shortestPaths[i][j] = graph.getMatrix()[i][j];
                }
            }
        }
        for (int i = 0; i < graph.getN(); i++) {
            shortestPaths[i][i] = 0;
        }
        for (int k = 0; k < graph.getN(); k++) {
            for (int i = 0; i < graph.getN(); i++) {
                for (int j = 0; j < graph.getN(); j++) {
                    if (shortestPaths[i][j] > shortestPaths[i][k] + shortestPaths[k][j]) {
                        shortestPaths[i][j] = shortestPaths[i][k] + shortestPaths[k][j];
                    }
                }
            }
        }
        return shortestPaths;
    }

}