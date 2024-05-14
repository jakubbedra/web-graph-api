package com.konfyrm.webgraphapi.domain.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ExecutionResponse {
    private int tasksInProgress;
    private long triggeredTimestamp;
    private long lastUpdateTimestamp;
}
