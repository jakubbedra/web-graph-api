package com.konfyrm.webgraphapi.algorithm;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SearchAlgorithms {

    private SearchAlgorithms() {
    }

    public static List<Integer> bfs(int[][] graph, int n, int startVertex) {
        boolean[] visited = new boolean[graph.length];
        for (int i = 0; i < graph.length; i++) {
            visited[i] = false;
        }
        return bfs(graph, n, startVertex, visited);
    }

    public static List<Integer> bfs(int[][] graph, int n, int startVertex, boolean[] visited) {
        Queue<Integer> queue = new LinkedList<>();
        visited[startVertex] = true;
        queue.add(startVertex);

        List<Integer> order = new LinkedList<>();
        while (!queue.isEmpty()) {
            int v = queue.poll();
            order.add(v);

            for (int u = 0; u < n; u++) {
                if (graph[v][u] == 1 && !visited[u]) {
                    visited[u] = true;
                    queue.add(u);
                }
            }
        }
        return order;
    }

}
