package com.konfyrm.webgraphapi.domain.response;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ConnectedComponentsResponse {
    private List<List<Integer>> weaklyConnectedComponents;
    private List<List<Integer>> stronglyConnectedComponents;
    private int wccCount;
    private int sccCount;
}
