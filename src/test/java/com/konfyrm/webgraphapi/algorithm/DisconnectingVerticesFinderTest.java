package com.konfyrm.webgraphapi.algorithm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

import java.util.Collections;
import java.util.List;

public class DisconnectingVerticesFinderTest {

    @Test
    public void findDisconnectingVertices_noDisconnectingVertices_emptyListReturned() {
        int[][] graph = {
                {0, 1, 1, 0, 1},
                {1, 0, 1, 1, 0},
                {1, 1, 0, 1, 0},
                {0, 1, 1, 0, 1},
                {1, 0, 0, 1, 0},
        };
        int n = 5;
        List<Integer> expectedVertices = Collections.emptyList();

        List<Integer> vertices = DisconnectingVerticesFinder.findDisconnectingVertices(graph, n);

        Assertions.assertEquals(expectedVertices, vertices);
    }

    @Test
    public void findDisconnectingVertices_oneDisconnectingVertex() {
        int[][] graph = {
                {0, 1, 0, 0, 0, 0, 1},
                {1, 0, 1, 0, 1, 1, 0},
                {0, 1, 0, 1, 0, 0, 0},
                {0, 0, 1, 0, 1, 0, 0},
                {0, 1, 0, 1, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 1, 0},
        };
        int n = 7;
        List<Integer> expectedVertices = List.of(1);

        List<Integer> vertices = DisconnectingVerticesFinder.findDisconnectingVertices(graph, n);

        Assertions.assertEquals(expectedVertices, vertices);
    }

    @Test
    public void findDisconnectingVertices_someDisconnectingVertices() {
        int[][] graph = {
                {0, 1, 0, 0},
                {1, 0, 1, 0},
                {0, 1, 0, 1},
                {0, 0, 1, 0},
        };
        int n = 4;
        List<Integer> expectedVertices = List.of(1, 2);

        List<Integer> vertices = DisconnectingVerticesFinder.findDisconnectingVertices(graph, n);

        Assertions.assertEquals(expectedVertices, vertices);
    }

    @Test
    public void findDisconnectingVerticesPairs_noDisconnectingVerticesPairs_emptyListReturned() {
        int[][] graph = {
                {0, 1, 1, 1, 1, 1},
                {1, 0, 1, 1, 1, 1},
                {1, 1, 0, 1, 1, 1},
                {1, 1, 1, 0, 1, 1},
                {1, 1, 1, 1, 0, 1},
                {1, 1, 1, 1, 1, 0},
        };
        int n = 6;
        List<Integer> expectedVertices = List.of();

        List<Integer> vertices = DisconnectingVerticesFinder.findDisconnectingVertices(graph, n);

        Assertions.assertEquals(expectedVertices, vertices);
    }

    @Test
    public void findDisconnectingVerticesPairs_someDisconnectingVerticesPairs() {
        int[][] graph = {
                {0, 1, 0, 0, 0, 0, 1, 1},
                {1, 0, 1, 0, 0, 1, 0, 0},
                {0, 1, 0, 1, 1, 1, 0, 0},
                {0, 0, 1, 0, 1, 0, 0, 0},
                {0, 0, 1, 1, 0, 1, 0, 0},
                {0, 1, 1, 0, 1, 0, 1, 0},
                {1, 0, 0, 0, 0, 1, 0, 1},
                {1, 0, 0, 0, 0, 0, 1, 0},
        };
        int n = 8;
        List<Pair<Integer, Integer>> expectedVertices = List.of(
                Pair.of(5, 0), Pair.of(6, 0), Pair.of(5, 1), Pair.of(6, 1), Pair.of(4, 2), Pair.of(5, 2)
        );

        List<Pair<Integer, Integer>> vertices = DisconnectingVerticesFinder.findDisconnectingVerticesPairs(graph, n);

        Assertions.assertEquals(expectedVertices, vertices);
    }

}