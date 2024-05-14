package com.konfyrm.webgraphapi.algorithm;

import org.springframework.data.util.Pair;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DisconnectingVerticesFinder {

    private DisconnectingVerticesFinder() {
        throw new IllegalStateException("Utils class should not be instantiated.");
    }

    // we assume that we only pass non-directed graphs!!!
    public static List<Integer> findDisconnectingVertices(int[][] graph, int n) {
        boolean[] visited = new boolean[n];
        List<Integer> disconnectingVertices = new LinkedList<>();
        for (int v = 0; v < n; v++) {
            Arrays.fill(visited,false);
            visited[v] = true;// the vertex does not exist :)
            List<Integer> bfsOrder = SearchAlgorithms.bfs(graph, n, (v + 1) % n, visited);
            if (bfsOrder.size() != n - 1) {
                disconnectingVertices.add(v);
            }
        }
        return disconnectingVertices;
    }

    public static List<Pair<Integer, Integer>> findDisconnectingVerticesPairs(int[][] graph, int n) {
        boolean[] visited = new boolean[n];
        List<Pair<Integer, Integer>> disconnectingVertices = new LinkedList<>();
        for (int v = 0; v < n; v++) {
            for (int u = v + 1; u < n; u++) {
                Arrays.fill(visited,false);
                visited[v] = true;// the vertex does not exist :)
                visited[u] = true;// the vertex does not exist :)
                int startVertex = lowestNumber(u, v, n);
                List<Integer> bfsOrder = SearchAlgorithms.bfs(graph, n, startVertex, visited);
                if (bfsOrder.size() != n - 2) {
                    disconnectingVertices.add(Pair.of(u, v));
                }
            }
        }
        return disconnectingVertices;
    }

    private static int lowestNumber(int u, int v, int n) {
        for (int i = 0; i < n; i++) {
            if (i != u && i != v) {
                return i;
            }
        }
        return -1;
    }

}
