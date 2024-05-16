package com.konfyrm.webgraphapi.domain.response;

import lombok.*;
import org.springframework.data.util.Pair;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DisconnectingVerticesResponse {
    private int disconnectingVerticesCount;
    private int disconnectingVerticesPairsCount;
    private List<Integer> disconnectingVertices;
    private List<Pair<Integer, Integer>> disconnectingVerticesPairs;
}
