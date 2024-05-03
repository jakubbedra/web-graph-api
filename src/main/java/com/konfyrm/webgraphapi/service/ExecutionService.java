package com.konfyrm.webgraphapi.service;

import com.konfyrm.webgraphapi.domain.entity.Execution;

import java.util.Optional;

public interface ExecutionService {
    String save(Execution execution);
    Optional<Execution> findByUuid(String uuid);
    boolean update(String uuid, int doneTasks, int newTasks);
}
