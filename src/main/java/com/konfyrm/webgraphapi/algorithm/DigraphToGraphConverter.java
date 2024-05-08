package com.konfyrm.webgraphapi.algorithm;

public class DigraphToGraphConverter {

    public static int[][] convert(int[][] digraph, int n) {
        int[][] graph = new int[n][n];

        for (int v = 0; v < n; v++) {
            for (int u = v; u < n; u++) {
                if (digraph[u][v] == 1 || digraph[v][u] == 1) {
                    graph[u][v] = 1;
                    graph[v][u] = 1;
                } else {
                    graph[u][v] = 0;
                    graph[v][u] = 0;
                }
            }
        }

        return graph;
    }

}
