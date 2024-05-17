package com.konfyrm.webgraphapi.algorithm;

import java.util.*;

public class SearchAlgorithms {

    private SearchAlgorithms() {
        throw new IllegalStateException("Utils class should not be instantiated.");
    }

    public static List<Integer> dfs(List<Integer>[] graph, int n, int startVertex, boolean[] visited) {
        List<Integer> order = new ArrayList<>(n);
        Stack<Integer> stack = new Stack<>();
        stack.push(startVertex);

        while (!stack.isEmpty()) {
            int v = stack.pop();

            if (!visited[v]) {
                order.add(v);
                visited[v] = true;

                for (int u: graph[v]) {
                    if (!visited[u]) {
                        stack.push(u);
                    }
                }
            }
        }

        return order;
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

    public static List<Integer> bfs(List<Integer>[] graph, int startVertex) {
        boolean[] visited = new boolean[graph.length];
        for (int i = 0; i < graph.length; i++) {
            visited[i] = false;
        }
        return bfs(graph, startVertex, visited);
    }

    public static List<Integer> bfs(List<Integer>[] graph, int startVertex, boolean[] visited) {
        Queue<Integer> queue = new LinkedList<>();
        visited[startVertex] = true;
        queue.add(startVertex);

        List<Integer> order = new LinkedList<>();
        while (!queue.isEmpty()) {
            int v = queue.poll();
            order.add(v);

            for (Integer u : graph[v]) {
                if (!visited[u]) {
                    visited[u] = true;
                    queue.add(u);
                }
            }
        }
        return order;
    }

}
