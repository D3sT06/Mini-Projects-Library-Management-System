package com.sahin.library_management.repository.jpa;

import com.sahin.library_management.infra.entity.jpa.LogAggregationEntity;
import com.sahin.library_management.infra.enums.LogAction;
import com.sahin.library_management.infra.enums.QueryTerm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LogAggregationRepository extends JpaRepository<LogAggregationEntity, Long> {

    Optional<LogAggregationEntity> findByBarcodeAndQueryTermAndAction(String barcode, QueryTerm term, LogAction action);
}
