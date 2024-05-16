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
        int[][] paths = multiply(graph, graph, n);
        int triangleCount = calculateTriangleCount(graph, paths, n);
        int tripletsCount = allTripletsCount(paths, n, triangleCount);
        return (3.0 * (double) triangleCount) / ((double) tripletsCount);
    }

    private static int[][] multiply(int[][] matrixA, int[][] matrixB, int n) {
        int[][] result = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(result[i], 0);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    result[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
        return result;
    }

    private static int calculateTriangleCount(int[][] graph, int[][] paths, int n) {
        int count = 0;
        for (int v = 0; v < n; v++) {
            for (int u = v + 1; u < n; u++) {
                if (graph[v][u] == 1) {
                    count += paths[v][u];
                }
            }
        }
        return count / 3;
    }

    private static int allTripletsCount(int[][] paths, int n, int trianglesCount) {
        int sum = 0;
        for (int v = 0; v < n; v++) {
            for (int u = 0; u < n; u++) {
                sum += paths[u][v];
            }
        }
        return (sum - 4 * trianglesCount) / 2;
    }

}
