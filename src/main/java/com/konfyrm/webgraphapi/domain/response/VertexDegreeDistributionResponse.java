package com.konfyrm.webgraphapi.domain.response;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class VertexDegreeDistributionResponse {
    private Map<Integer, Integer> inDegreeDistribution;
    private Map<Integer, Integer> outDegreeDistribution;
    // todo: power function elements
}
