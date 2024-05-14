package com.konfyrm.webgraphapi.algorithm;

import java.util.*;

public class DigraphToGraphConverter {

    private DigraphToGraphConverter() {
        throw new IllegalStateException("Utils class should not be instantiated.");
    }

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

    public static List<Integer>[] convert(List<Integer>[] digraph, int n) {
        Set<Integer>[] graph = new Set[n];
        for (int i = 0; i < graph.length; i++) {
            graph[i] = new HashSet<>();
        }

        for (int v = 0; v < n; v++) {
            for (Integer u : digraph[v]) {
                graph[v].add(u);
                graph[u].add(v);
            }
        }

        List<Integer>[] graphList = new List[n];
        for (int i = 0; i < n; i++) {
            graphList[i] = new ArrayList<>(graph[i]);
        }
        return graphList;
    }

}
