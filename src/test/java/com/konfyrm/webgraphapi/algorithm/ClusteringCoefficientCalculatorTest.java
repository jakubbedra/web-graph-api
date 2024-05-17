package com.konfyrm.webgraphapi.algorithm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClusteringCoefficientCalculatorTest {

    @Test
    public void computeGlobalTest() {
        int[][] graph = {
                //1, 2, 3, 4, 5, 6, 7
                { 0, 1, 1, 1, 0, 0, 0},
                { 1, 0, 0, 0, 0, 1, 1},
                { 1, 0, 0, 1, 0, 0, 1},
                { 1, 0, 1, 0, 1, 1, 0},
                { 0, 0, 0, 1, 0, 1, 0},
                { 0, 1, 0, 1, 1, 0, 0},
                { 0, 1, 1, 0, 0, 0, 0}
        };
        int n = 7;
        double expected = 0.3;

        double global = ClusteringCoefficientCalculator.computeGlobal(graph, n);

        Assertions.assertEquals(expected, global);
    }

}
