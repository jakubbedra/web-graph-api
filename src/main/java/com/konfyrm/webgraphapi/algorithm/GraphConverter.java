package com.konfyrm.webgraphapi.algorithm;

import java.util.*;

public class GraphConverter {

    private GraphConverter() {
        throw new IllegalStateException("Utils class should not be instantiated.");
    }

    public static int[][] digraphToAdjacencyMatrixGraph(List<Integer>[] digraph, int n) {
        int[][] graph = new int[n][n];
        for (int v = 0; v < n; v++) {
            for (int u = 0; u < n; u++) {
                graph[v][u] = 0;
            }
        }
        for (int v = 0; v < n; v++) {
            for (Integer u : digraph[v]) {
                if (u < n)
                    graph[v][u] = graph[u][v] = 1;
            }
        }

        return graph;
    }

    public static List<Integer>[] reverseDigraph(List<Integer>[] digraph, int n) {
        List<Integer>[] reversed = new List[n];
        for (int v = 0; v < n; v++) {
            reversed[v] = new LinkedList<>();
        }

        for (int v = 0; v < n; v++) {
            for (int u : digraph[v]) {
                reversed[u].add(v);
            }
        }

        return reversed;
    }

    public static List<Integer>[] digraphToGraph(List<Integer>[] digraph, int n) {
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
