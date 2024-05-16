package com.konfyrm.webgraphapi.algorithm;

import com.konfyrm.webgraphapi.domain.model.UrlGraph;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class WCCFinder {

    private WCCFinder() {
        throw new IllegalStateException("Utils class should not be instantiated.");
    }

    public static List<List<Integer>> execute(UrlGraph graph) {
        List<List<Integer>> wcc = new LinkedList<>();
        List<Integer>[] neighbours = graph.getNeighbours();

        List<Integer>[] convertedMatrix = GraphConverter.digraphToGraph(neighbours, graph.getN());

        boolean[] visited = new boolean[graph.getN()];
        Arrays.fill(visited, false);

        for (int v = 0; v < graph.getN(); v++) {
            if (!visited[v]) {
                List<Integer> bfsOrder = SearchAlgorithms.bfs(convertedMatrix, v);
                bfsOrder.forEach(u -> visited[u] = true);
                wcc.add(bfsOrder);
            }
        }

        return wcc;
    }

}

