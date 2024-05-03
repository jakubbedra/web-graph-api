package com.konfyrm.webgraphapi.service.impl;

import com.konfyrm.webgraphapi.ExecutionRepository;
import com.konfyrm.webgraphapi.domain.entity.Execution;
import com.konfyrm.webgraphapi.service.ExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ExecutionServiceImpl implements ExecutionService {

    private final ExecutionRepository executionRepository;

    @Autowired
    public ExecutionServiceImpl(
            ExecutionRepository executionRepository
    ) {
        this.executionRepository = executionRepository;
    }

    @Override
    public String save(Execution execution) {
        String uuid = String.valueOf(UUID.randomUUID());
        execution.setUuid(uuid);
        execution.setTriggeredTimestamp(System.currentTimeMillis());
        executionRepository.save(execution);
        return uuid;
    }

    @Override
    public Optional<Execution> findByUuid(String uuid) {
        return executionRepository.findById(uuid);
    }

    @Override
    public boolean update(String uuid, int doneTasks, int newTasks) {
        Optional<Execution> executionOptional = executionRepository.findById(uuid);
        if (executionOptional.isEmpty()) {
            return false;
        }
        Execution execution = executionOptional.get();
        int tasksInProgress = execution.getTasksInProgress();
        execution.setTasksInProgress(tasksInProgress - doneTasks + newTasks);
        execution.setLastUpdateTimestamp(System.currentTimeMillis());
        executionRepository.save(execution);
        return true;
    }

}