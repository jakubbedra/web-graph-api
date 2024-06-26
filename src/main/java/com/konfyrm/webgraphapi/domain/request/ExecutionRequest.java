package com.konfyrm.webgraphapi.domain.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ExecutionRequest {
    private String url;
    private boolean downloadFiles;
}