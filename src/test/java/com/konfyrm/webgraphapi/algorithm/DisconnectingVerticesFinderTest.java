package com.konfyrm.webgraphapi.algorithm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

import java.util.Collections;
import java.util.List;

public class DisconnectingVerticesFinderTest {

    @Test
    public void findDisconnectingVertices_noDisconnectingVertices_emptyListReturned() {
        int n = 5;
        List<Integer>[] graph = new List[n];
        graph[0] = List.of(1,2,4);
        graph[1] = List.of(0,2,3);
        graph[2] = List.of(0,1,3);
        graph[3] = List.of(1,2,4);
        graph[4] = List.of(1,3);
        List<Integer> expectedVertices = Collections.emptyList();

        List<Integer> vertices = DisconnectingVerticesFinder.findDisconnectingVertices(graph, n);

        Assertions.assertEquals(expectedVertices, vertices);
    }

    @Test
    public void findDisconnectingVertices_oneDisconnectingVertex() {
        int n = 7;
        List<Integer>[] graph = new List[n];
        graph[0] = List.of(1,6);
        graph[1] = List.of(0,2,4,5);
        graph[2] = List.of(1,3);
        graph[3] = List.of(2,4);
        graph[4] = List.of(1,3);
        graph[5] = List.of(1,6);
        graph[6] = List.of(0,5);
        List<Integer> expectedVertices = List.of(1);

        List<Integer> vertices = DisconnectingVerticesFinder.findDisconnectingVertices(graph, n);

        Assertions.assertEquals(expectedVertices, vertices);
    }

    @Test
    public void findDisconnectingVertices_someDisconnectingVertices() {
        int n = 4;
        List<Integer>[] graph = new List[n];
        graph[0] = List.of(1);
        graph[1] = List.of(0,2);
        graph[2] = List.of(1,3);
        graph[3] = List.of(2);
        List<Integer> expectedVertices = List.of(1, 2);

        List<Integer> vertices = DisconnectingVerticesFinder.findDisconnectingVertices(graph, n);

        Assertions.assertEquals(expectedVertices, vertices);
    }

    @Test
    public void findDisconnectingVerticesPairs_noDisconnectingVerticesPairs_emptyListReturned() {
        int n = 6;
        List<Integer>[] graph = new List[n];
        graph[0] = List.of(1,2,3,4,5);
        graph[1] = List.of(0,2,3,4,5);
        graph[2] = List.of(0,1,3,4,5);
        graph[3] = List.of(0,1,2,4,5);
        graph[4] = List.of(0,1,2,3,5);
        graph[5] = List.of(0,1,2,3,4);
        List<Integer> expectedVertices = List.of();

        List<Integer> vertices = DisconnectingVerticesFinder.findDisconnectingVertices(graph, n);

        Assertions.assertEquals(expectedVertices, vertices);
    }

    @Test
    public void findDisconnectingVerticesPairs_someDisconnectingVerticesPairs() {
        int n = 8;
        List<Integer>[] graph = new List[n];
        graph[0] = List.of(1,6,7);
        graph[1] = List.of(0,2,5);
        graph[2] = List.of(1,3,4,5);
        graph[3] = List.of(2,4);
        graph[4] = List.of(2,3,5);
        graph[5] = List.of(1,2,4,6);
        graph[6] = List.of(0,5,7);
        graph[7] = List.of(0,6);
        List<Pair<Integer, Integer>> expectedVertices = List.of(
                Pair.of(5, 0), Pair.of(6, 0), Pair.of(5, 1), Pair.of(6, 1), Pair.of(4, 2), Pair.of(5, 2)
        );

        List<Pair<Integer, Integer>> vertices = DisconnectingVerticesFinder.findDisconnectingVerticesPairs(graph, n);

        Assertions.assertEquals(expectedVertices, vertices);
    }

}