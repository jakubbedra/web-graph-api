package com.konfyrm.webgraphapi.domain.message;

import lombok.*;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UrlVisitResult {
    private String executionUuid;
    private String startUrl;
    private Map<String, Set<String>> neighbours;
}

