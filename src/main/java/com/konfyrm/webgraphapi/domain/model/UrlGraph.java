package com.konfyrm.webgraphapi.domain.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class UrlGraph {
    private int n;
    private int m;
    private int[][] matrix;
    private Map<String, Integer> urlsToIndices;
}

