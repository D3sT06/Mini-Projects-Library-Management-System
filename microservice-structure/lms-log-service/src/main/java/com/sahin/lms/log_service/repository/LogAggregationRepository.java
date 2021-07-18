package com.sahin.lms.log_service.repository;

import com.sahin.lms.infra_entity.log.jpa.LogAggregationEntity;
import com.sahin.lms.infra_enum.LogAction;
import com.sahin.lms.infra_enum.QueryTerm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LogAggregationRepository extends JpaRepository<LogAggregationEntity, Long> {

    Optional<LogAggregationEntity> findByBarcodeAndQueryTermAndAction(String barcode, QueryTerm term, LogAction action);
}
