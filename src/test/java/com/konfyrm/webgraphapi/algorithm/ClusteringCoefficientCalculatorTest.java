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

    @Test
    public void computeGlobal_completeGraph() {
        int[][] graph = {
                //1, 2, 3, 4, 5, 6, 7
                { 0, 1, 1, 1, 1, 1, 1},
                { 1, 0, 1, 1, 1, 1, 1},
                { 1, 1, 0, 1, 1, 1, 1},
                { 1, 1, 1, 0, 1, 1, 1},
                { 1, 1, 1, 1, 0, 1, 1},
                { 1, 1, 1, 1, 1, 0, 1},
                { 1, 1, 1, 1, 1, 1, 0}
        };
        int n = 7;
        double expected = 1.0;

        double global = ClusteringCoefficientCalculator.computeGlobal(graph, n);

        Assertions.assertEquals(expected, global);
    }

    @Test
    public void computeGlobal_pathGraph() {
        int[][] graph = {
                //1, 2, 3, 4, 5, 6, 7
                { 0, 1, 0, 0, 0, 0, 0},
                { 1, 0, 1, 0, 0, 0, 0},
                { 0, 1, 0, 1, 0, 0, 0},
                { 0, 0, 1, 0, 1, 0, 0},
                { 0, 0, 0, 1, 0, 1, 0},
                { 0, 0, 0, 0, 1, 0, 1},
                { 0, 0, 0, 0, 0, 1, 0}
        };
        int n = 7;
        double expected = 0.0;

        double global = ClusteringCoefficientCalculator.computeGlobal(graph, n);

        Assertions.assertEquals(expected, global);
    }

    @Test
    public void computeGlobal_randomGraph() {
        int[][] graph = {
                //1, 2, 3, 4, 5, 6, 7
                { 0, 1, 1, 1, 1, 1, 1,      0, 0, 0, 0, 0, 0, 0,        0, 0, 0, 0, 0, 0, 0 },
                { 1, 0, 1, 1, 1, 1, 1,      0, 0, 0, 0, 0, 0, 0,        0, 0, 0, 0, 0, 0, 0 },
                { 1, 1, 0, 1, 1, 1, 1,      0, 0, 0, 0, 0, 0, 0,        0, 0, 0, 0, 0, 0, 0 },
                { 1, 1, 1, 0, 1, 1, 1,      0, 0, 0, 0, 0, 0, 0,        0, 0, 0, 0, 0, 0, 0 },
                { 1, 1, 1, 1, 0, 1, 1,      0, 0, 0, 0, 0, 0, 0,        0, 0, 0, 0, 0, 0, 0 },
                { 1, 1, 1, 1, 1, 0, 1,      0, 0, 0, 0, 0, 0, 0,        0, 0, 0, 0, 0, 0, 0 },
                { 1, 1, 1, 1, 1, 1, 0,      1, 0, 0, 0, 0, 0, 0,        0, 0, 0, 0, 0, 0, 0 },

                { 0, 0, 0, 0, 0, 0, 1,      0, 1, 0, 0, 0, 0, 0,        0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0,      1, 0, 1, 0, 0, 0, 0,        0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0,      0, 1, 0, 1, 0, 0, 0,        0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0,      0, 0, 1, 0, 1, 0, 0,        0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0,      0, 0, 0, 1, 0, 1, 0,        0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0,      0, 0, 0, 0, 1, 0, 1,        0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0,      0, 0, 0, 0, 0, 1, 0,        1, 0, 0, 0, 0, 0, 0 },

                { 0, 0, 0, 0, 0, 0, 0,      0, 0, 0, 0, 0, 0, 1,        0, 1, 1, 1, 1, 1, 1 },
                { 0, 0, 0, 0, 0, 0, 0,      0, 0, 0, 0, 0, 0, 0,        1, 0, 1, 1, 1, 1, 1 },
                { 0, 0, 0, 0, 0, 0, 0,      0, 0, 0, 0, 0, 0, 0,        1, 1, 0, 1, 1, 1, 1 },
                { 0, 0, 0, 0, 0, 0, 0,      0, 0, 0, 0, 0, 0, 0,        1, 1, 1, 0, 1, 1, 1 },
                { 0, 0, 0, 0, 0, 0, 0,      0, 0, 0, 0, 0, 0, 0,        1, 1, 1, 1, 0, 1, 1 },
                { 0, 0, 0, 0, 0, 0, 0,      0, 0, 0, 0, 0, 0, 0,        1, 1, 1, 1, 1, 0, 1 },
                { 0, 0, 0, 0, 0, 0, 0,      0, 0, 0, 0, 0, 0, 0,        1, 1, 1, 1, 1, 1, 0 }
        };
        int n = 21;
        double expected = 0.0;

        double global = ClusteringCoefficientCalculator.computeGlobal(graph, n);
//        double global = ClusteringCoefficientCalculator.computeGlobalJgrapht(graph, n);

        Assertions.assertEquals(expected, global);

        // jgraph: 0.9170305676855895
        // shit:   0.9170305676855895
    }

}
