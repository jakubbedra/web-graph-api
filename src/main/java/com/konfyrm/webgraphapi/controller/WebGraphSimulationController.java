package com.konfyrm.webgraphapi.controller;

import com.konfyrm.webgraphapi.domain.request.SimulateAttackRequest;
import com.konfyrm.webgraphapi.domain.request.SimulateMalfunctionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("api/v1/graphs/{executionUuid}/simulation")
public interface WebGraphSimulationController {

    @PostMapping("/attack")
    ResponseEntity<?> simulateAttack(@PathVariable("executionUuid") String executionUuid, @RequestBody SimulateAttackRequest request);

    @PostMapping("/malfunction")
    ResponseEntity<?> simulateMalfunction(@PathVariable("executionUuid") String executionUuid, @RequestBody SimulateMalfunctionRequest request);

}