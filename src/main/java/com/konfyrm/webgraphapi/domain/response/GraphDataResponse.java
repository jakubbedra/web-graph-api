package com.konfyrm.webgraphapi.domain.response;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class GraphDataResponse {
    private ConnectedComponentsResponse connectedComponents;
    private VertexDegreeDistributionResponse degreeDistribution;
    private GraphDistancesResponse distances;
    private DisconnectingVerticesResponse disconnectingVertices;
    private Map<String, Integer> removedVertices;
    private int m;
}
