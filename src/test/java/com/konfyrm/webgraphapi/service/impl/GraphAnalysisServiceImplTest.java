package com.konfyrm.webgraphapi.service.impl;

import com.konfyrm.webgraphapi.algorithm.FloydWarshallAlgorithm;
import com.konfyrm.webgraphapi.domain.model.UrlGraph;
import com.konfyrm.webgraphapi.domain.response.GraphDistancesResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

public class GraphAnalysisServiceImplTest {

    private final GraphAnalysisServiceImpl graphAnalysisService;

    public GraphAnalysisServiceImplTest() {
        this.graphAnalysisService = new GraphAnalysisServiceImpl();
    }

    @Test
    public void calculateDistances_urlGraph() {
        int n = 7;
        int m = 7;
        List<Integer>[] neighbours = new List[n];
        neighbours[0] = List.of(1, 2);
        neighbours[1] = List.of(3);
        neighbours[2] = List.of(3);
        neighbours[3] = List.of(4);
        neighbours[4] = List.of(5,6);
        neighbours[5] = List.of();
        neighbours[6] = List.of();
        UrlGraph urlGraph = UrlGraph.builder()
                .n(n)
                .m(m)
                .neighbours(neighbours)
                .build();

        int expectedDiameter = 8;
        int expectedRadius = 5;

        GraphDistancesResponse graphDistancesResponse = graphAnalysisService.calculateDistances(urlGraph);

        Assertions.assertEquals(expectedDiameter, graphDistancesResponse.getDiameter());
        Assertions.assertEquals(expectedRadius, graphDistancesResponse.getRadius());
    }

    @Test
    public void calculateDistances_simpleDigraph() {
        try (MockedStatic<FloydWarshallAlgorithm> mockedStatic = mockStatic(FloydWarshallAlgorithm.class)) {
            mockedStatic.when(() -> FloydWarshallAlgorithm.execute(any())).thenReturn(sampleDistanceMatrix());
            UrlGraph graph = UrlGraph.builder()
                    .n(7)
                    .m(7)
                    .build();
            int expectedDiameter = 8;
            int expectedRadius = 5;

            GraphDistancesResponse graphDistancesResponse = graphAnalysisService.calculateDistances(graph);

            Assertions.assertEquals(expectedDiameter, graphDistancesResponse.getDiameter());
            Assertions.assertEquals(expectedRadius, graphDistancesResponse.getRadius());
        }
    }

    // https://www.baeldung.com/cs/graphs-eccentricity-radius-diameter-center-periphery
    private static int[][] sampleDistanceMatrix() {
        return new int[][]{
                //A, B, C, D, E, F, G
                { 0, 1, 1, 3, 6, 8, 7},
                { Integer.MIN_VALUE, 0, Integer.MIN_VALUE, 2, 5, 7, 6},
                { Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 3, 6, 8, 7},
                { Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 3, 5, 4},
                { Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 2, 1},
                { Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, 0, Integer.MIN_VALUE},
                { Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, 0}
        };
    }

    /*
    return new int[][]{
                //A, B, C, D, E, F, G    e(v)
                { 0, 1, 1, 3, 6, 8, 7},   8
                { -, 0, -, 2, 5, 7, 6},   7
                { -, -, 0, 3, 6, 8, 7},   8
                { -, -, -, 0, 3, 5, 4},   5
                { -, -, -, -, 0, 2, 1},   6
                { -, -, -, -, -, 0, -},   8
                { -, -, -, -, -, -, 0}    7
        };
     */
}
