package com.sahin.lms.log_service.repository;

import com.sahin.lms.infra.entity.jpa.LogAggregationEntity;
import com.sahin.lms.infra.enums.LogAction;
import com.sahin.lms.infra.enums.QueryTerm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LogAggregationRepository extends JpaRepository<LogAggregationEntity, Long> {

    Optional<LogAggregationEntity> findByBarcodeAndQueryTermAndAction(String barcode, QueryTerm term, LogAction action);
}
