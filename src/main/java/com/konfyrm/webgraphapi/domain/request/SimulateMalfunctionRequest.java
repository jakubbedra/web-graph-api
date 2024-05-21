package com.konfyrm.webgraphapi.domain.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SimulateMalfunctionRequest {
    private int randomVerticesCount;
    private int repetitions;
}
