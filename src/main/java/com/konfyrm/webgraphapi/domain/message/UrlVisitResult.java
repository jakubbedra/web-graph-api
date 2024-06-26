package com.konfyrm.webgraphapi.domain.message;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UrlVisitResult {
    private String executionUuid;
    private List<UrlNode> nodes;
}
