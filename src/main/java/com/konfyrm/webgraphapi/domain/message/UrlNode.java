package com.konfyrm.webgraphapi.domain.message;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UrlNode {
    private String url;
    private Set<String> neighbours;
}

