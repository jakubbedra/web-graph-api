package com.konfyrm.webgraphapi.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Execution {
    @Id
    private String uuid;
    private int tasksInProgress;
    private long triggeredTimestamp;
    private long lastUpdateTimestamp;
    private boolean downloadFiles;
}
