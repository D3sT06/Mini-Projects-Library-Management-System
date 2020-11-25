package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity.LogAggregationEntity;
import com.sahin.library_management.infra.enums.LogAction;
import com.sahin.library_management.infra.enums.QueryTerm;
import io.swagger.annotations.Api;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LogAggregationRepository extends JpaRepository<LogAggregationEntity, Long> {

    Optional<LogAggregationEntity> findByBarcodeAndQueryTermAndAction(String barcode, QueryTerm term, LogAction action);
}
