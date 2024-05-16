package com.konfyrm.webgraphapi.domain.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ClusteringCoefficientsResponse {
    private double global;
    private double[] local;
}