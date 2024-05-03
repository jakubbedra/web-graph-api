package com.konfyrm.webgraphapi.controller.impl;

import com.konfyrm.webgraphapi.controller.WebGraphController;
import com.konfyrm.webgraphapi.domain.entity.Execution;
import com.konfyrm.webgraphapi.service.ExecutionService;
import com.konfyrm.webgraphapi.service.GraphAnalysisService;
import com.konfyrm.webgraphapi.service.WebGraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class WebGraphControllerImpl implements WebGraphController {

    private final ExecutionService executionService;
    private final WebGraphService webGraphService;
    private final GraphAnalysisService graphAnalysisService;

    @Autowired
    public WebGraphControllerImpl(
            @Qualifier("executionServiceImpl") ExecutionService executionService,
            @Qualifier("webGraphServiceImpl") WebGraphService webGraphService
    ) {
        this.executionService = executionService;
        this.webGraphService = webGraphService;
    }

    @Override
    public ResponseEntity<?> getGraph(String executionUuid) {
        Optional<Execution> executionOptional = executionService.findByUuid(executionUuid);
        if (executionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // todo
        return null;
    }

}
