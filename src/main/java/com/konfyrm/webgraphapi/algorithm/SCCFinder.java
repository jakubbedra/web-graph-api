package com.konfyrm.webgraphapi.algorithm;

import com.konfyrm.webgraphapi.domain.model.UrlGraph;

import java.util.*;

public class SCCFinder {

    private SCCFinder() {
        throw new IllegalStateException("Utils class should not be instantiated.");
    }

    private static void fillOrder(List<Integer>[] graph, int startVertex, boolean visited[], Stack<Integer> stack) {
        Stack<Integer> stackU = new Stack<>();
        stackU.push(startVertex);

        while (!stackU.isEmpty()) {
            int v = stackU.peek();
            boolean allVisited = true;

            for (int u : graph[v]) {
                if (!visited[u]) {
                    visited[u] = true;
                    stackU.push(u);
                    allVisited = false;
                    break;
                }
            }

            if (allVisited) {
                int u = stackU.pop();
                stack.push(u);
            }
        }
    }

    private static List<List<Integer>> getSCCs(List<Integer>[] graph, int n) {
        List<List<Integer>> scc = new LinkedList<>();

        Stack<Integer> stack = new Stack<>();
        boolean visited[] = new boolean[n];
        Arrays.fill(visited, false);

        for (int v = 0; v < n; v++) {
            if (!visited[v]) {
                fillOrder(graph, v, visited, stack);
            }
        }

        List<Integer>[] transpose = GraphConverter.reverseDigraph(graph, n);

        Arrays.fill(visited, false);

        while (!stack.isEmpty()) {
            int v = stack.pop();

            if (!visited[v]) {
                List<Integer> dfsOrder = SearchAlgorithms.dfs(transpose, n, v, visited);
                scc.add(dfsOrder);
            }
        }

        return scc;
    }

    public static List<List<Integer>> execute(UrlGraph graph) {
        return getSCCs(graph.getNeighbours(), graph.getN());
    }

}

