package com.konfyrm.webgraphapi.domain.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class GraphDistancesResponse {
    private int radius;
    private int diameter;
    private int[][] distances;
    private double avgDistance;
}