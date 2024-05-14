package com.konfyrm.webgraphapi.algorithm;

import com.konfyrm.webgraphapi.domain.model.UrlGraph;

import java.util.LinkedList;
import java.util.List;

public class SCCFinder {

    private SCCFinder() {
        throw new IllegalStateException("Utils class should not be instantiated.");
    }

    public static List<List<Integer>> execute(UrlGraph graph) {
        List<List<Integer>> scc = new LinkedList<>();

        // todo

        return scc;
    }

}

