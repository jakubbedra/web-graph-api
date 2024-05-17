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
//        int[][] paths = multiply(graph, graph, n);
//        int triangleCount = calculateTriangleCount(graph, paths, n);
//        int tripletsCount = triangleCount + openTripletsCount(graph, paths, n, triangleCount);
//        return (3.0 * (double) triangleCount) / ((double) tripletsCount);
        int tripleTriangleCount = 0;
        int tripletCount = 0;

        // Iterate over all possible triplets
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    // Check if nodes i, j, k form a triplet
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

        // Calculate the global clustering coefficient
        if (tripletCount == 0) {
            return 0.0;
        }
        return (double) tripleTriangleCount / tripletCount;
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

    private static int openTripletsCount(int[][] graph, int[][] paths, int n, int trianglesCount) {
//        int sum = 0;//2x open triplets + 6x triangles
//        for (int v = 0; v < n; v++) {
//            for (int u = 0; u < n; u++) {
//                sum += paths[u][v];
//            }
//        }
//        return (sum - 4 * trianglesCount) / 2;
        int sum = 0;
        for (int v = 0; v < n; v++) {
            for (int u = 0; u < n; u++) {
                if (graph[u][v] == 0) {
                    sum += paths[u][v];
                }
            }
        }
        return sum;
    }

}
