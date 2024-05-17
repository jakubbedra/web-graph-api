package com.konfyrm.webgraphapi.algorithm;

import java.util.*;

public class InAndOutComponentFinder {

    private InAndOutComponentFinder() {
        throw new IllegalStateException("Utils class should not be instantiated.");
    }

    public static Map<Integer, List<Integer>> findInComponents(List<Integer>[] graph, int n, List<List<Integer>> sccs) {
        Map<Integer, List<Integer>> inComponents = new HashMap<>();
        List<Integer>[] reversed = GraphConverter.reverseDigraph(graph, n);
        for (List<Integer> scc : sccs) {
            Set<Integer> inComponent = new HashSet<>();
            boolean[] visited = new boolean[n];
            Arrays.fill(visited, false);
            for (Integer v : scc) {
                List<Integer> dfsOrder = SearchAlgorithms.dfs(reversed, n, v, visited);
                inComponent.addAll(dfsOrder);
            }
            int id = scc.stream().mapToInt(x -> x).min().orElse(0);
            inComponents.put(id, inComponent.stream().toList());
        }
        return inComponents;
    }

    public static Map<Integer, List<Integer>> findOutComponents(List<Integer>[] graph, int n, List<List<Integer>> sccs) {
        Map<Integer, List<Integer>> outComponents = new HashMap<>();
        for (List<Integer> scc : sccs) {
            Set<Integer> outComponent = new HashSet<>();
            boolean[] visited = new boolean[n];
            Arrays.fill(visited, false);
            for (Integer v : scc) {
                List<Integer> dfsOrder = SearchAlgorithms.dfs(graph, n, v, visited);
                outComponent.addAll(dfsOrder);
            }
            int id = scc.stream().mapToInt(x -> x).min().orElse(0);
            outComponents.put(id, outComponent.stream().toList());
        }
        return outComponents;
    }

}