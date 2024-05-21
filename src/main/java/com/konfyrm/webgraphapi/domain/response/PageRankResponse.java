package com.konfyrm.webgraphapi.domain.response;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PageRankResponse {
    private Double[] pageRank;
    private Map<Double, Integer> distribution;
    private double a;
    private double b;
    private int iterationsCount;
}
