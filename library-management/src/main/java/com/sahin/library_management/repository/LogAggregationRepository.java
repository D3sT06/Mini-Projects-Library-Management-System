package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity.LogAggregationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogAggregationRepository extends JpaRepository<LogAggregationEntity, Long> {
}
