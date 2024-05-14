package com.konfyrm.webgraphapi.domain.model;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UrlGraph {
    private int n;
    private int m;
    private List<Integer>[] neighbours;
    private Map<String, Integer> urlsToIndices;
}
