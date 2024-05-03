package com.konfyrm.webgraphapi;

import com.konfyrm.webgraphapi.domain.entity.Execution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExecutionRepository extends JpaRepository<Execution, String> {
}
