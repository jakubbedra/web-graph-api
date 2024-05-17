package com.konfyrm.webgraphapi.domain.response;


import lombok.*;

import java.util.List;
import java.util.Map;

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
    private Map<Integer, List<Integer>> inComponents;
    private Map<Integer, List<Integer>> outComponents;
}

