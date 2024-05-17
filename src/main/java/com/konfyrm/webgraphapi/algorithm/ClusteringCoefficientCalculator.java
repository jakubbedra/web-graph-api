package com.konfyrm.webgraphapi.algorithm;

import java.util.Arrays;

public class ClusteringCoefficientCalculator {

    private ClusteringCoefficientCalculator() {
        throw new IllegalStateException("Utils class should not be instantiated.");
    }

    public static double computeLocal(int[][] graph, int n, int v) {
        int degree = 0;
        for (int u = 0; u < n; u++) {
            degree += graph[v][u];
        }
        if (degree == 1) {
            return 1.0;
        }
        int connectedNeighbours = 0;
        for (int u = 0; u < n; u++) {
            if (graph[v][u] == 1) {
                for (int w = u + 1; w < n; w++) {
                    if (graph[v][w] == 1 && graph[u][w] == 1) {
                        connectedNeighbours++;
                    }
                }
            }
        }
        return (double) connectedNeighbours / ((double) (degree * (degree - 1)));
    }

    public static double computeGlobal(int[][] graph, int n) {
        int tripleTriangleCount = 0;
        int tripletCount = 0;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    if (graph[i][j] == 1 && graph[j][k] == 1) {
                        tripletCount++;
                        if (graph[i][k] == 1) {
                            tripleTriangleCount++;
                        }
                    }
                    if (graph[i][k] == 1 && graph[k][j] == 1) {
                        tripletCount++;
                        if (graph[i][j] == 1) {
                            tripleTriangleCount++;
                        }
                    }
                    if (graph[j][i] == 1 && graph[i][k] == 1) {
                        tripletCount++;
                        if (graph[j][k] == 1) {
                            tripleTriangleCount++;
                        }
                    }
                }
            }
        }

        if (tripletCount == 0) {
            return 0.0;
        }
        return (double) tripleTriangleCount / tripletCount;
    }

}
