package com.konfyrm.webgraphapi.domain.response;

import lombok.*;

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
}