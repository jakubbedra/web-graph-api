package com.konfyrm.webgraphapi.algorithm;

import org.springframework.data.util.Pair;

import java.util.Arrays;
import java.util.List;

public class PageRank {

    private static final double CONVERGENCE_THRESHOLD = 0.00000000001;

    private PageRank() {
        throw new IllegalStateException("Utils class should not be instantiated.");
    }

    public static Pair<Double[], Integer> execute(List<Integer>[] graph, int n, double dampingFactor) {
        List<Integer>[] reversed = GraphConverter.reverseDigraph(graph, n);
        Double[] pageRank = new Double[n];
        Double[] previousRank;
        Arrays.fill(pageRank, 1.0 / (double)n);
        int iterationsCount = 0;

        do {
            previousRank = Arrays.copyOf(pageRank, n);

            for (int v = 0; v < n; v++) {
                double sum = 0.0;
                // pages u that contain link to v
                for (Integer u : reversed[v]) {
                    sum += previousRank[u] / (double) graph[u].size();
                }
                pageRank[v] = (1-dampingFactor)/((double) n) + dampingFactor * sum;
            }
            iterationsCount++;
        } while (!converged(pageRank, previousRank, n));
        return Pair.of(pageRank, iterationsCount);
    }

    public static Pair<Double[], Integer> execute(List<Integer>[] graph, int n) {
        List<Integer>[] reversed = GraphConverter.reverseDigraph(graph, n);
        Double[] pageRank = new Double[n];
        Double[] previousRank;
        Arrays.fill(pageRank, 1.0 / (double)n);
        int iterationsCount = 0;

        do {
            previousRank = Arrays.copyOf(pageRank, n);

            for (int v = 0; v < n; v++) {
                double sum = 0.0;
                // pages u that contain link to v
                for (Integer u : reversed[v]) {
                    sum += previousRank[u] / (double) graph[u].size();
                }
                pageRank[v] = sum;
            }
            iterationsCount++;
        } while (!converged(pageRank, previousRank, n));
        return Pair.of(pageRank, iterationsCount);
    }

    private static boolean converged(Double[] currentRank, Double[] previousRank, int n) {
        for (int i = 0; i < n; i++) {
            if (Math.abs(currentRank[i] - previousRank[i]) > CONVERGENCE_THRESHOLD) {
                return false;
            }
        }
        return true;
    }

}

