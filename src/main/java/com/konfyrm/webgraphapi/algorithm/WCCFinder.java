package com.konfyrm.webgraphapi.algorithm;

import com.konfyrm.webgraphapi.domain.model.UrlGraph;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class WCCFinder {

    public static List<List<Integer>> execute(UrlGraph graph) {
        List<List<Integer>> wcc = new LinkedList<>();
        int[][] matrix = graph.getMatrix();
        int[][] convertedMatrix = DigraphToGraphConverter.convert(matrix, graph.getN());

        boolean[] visited = new boolean[graph.getN()];
        Arrays.fill(visited, false);

        for (int v = 0; v < graph.getN(); v++) {
            if (!visited[v]) {
                List<Integer> bfsOrder = SearchAlgorithms.bfs(convertedMatrix, graph.getN(), v);
                bfsOrder.forEach(u -> visited[u] = true);
                wcc.add(bfsOrder);
            }
        }

        return wcc;
    }

}

