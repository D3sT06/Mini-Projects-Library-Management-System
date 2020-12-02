package com.sahin.library_management.service;

import com.sahin.library_management.factory.ChronoUnitFactory;
import com.sahin.library_management.infra.entity.LogAggregationEntity;
import com.sahin.library_management.infra.enums.LogAction;
import com.sahin.library_management.infra.enums.QueryTerm;
import com.sahin.library_management.infra.enums.TimeUnit;
import com.sahin.library_management.infra.model.log.LogAggregation;
import com.sahin.library_management.mapper.LogAggregationMapper;
import com.sahin.library_management.repository.LogAggregationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

@Service
public class LogAggregationService {

    @Autowired
    private LogAggregationMapper logAggregationMapper;

    @Autowired
    private LogAggregationRepository logAggregationRepository;

    @Autowired
    private ChronoUnitFactory chronoUnitFactory;

    @Transactional
    public void saveAll(Collection<LogAggregation> logs) {

        Collection<LogAggregationEntity> entities = logAggregationMapper.toEntityCollection(logs);
        logAggregationRepository.saveAll(entities);
    }

    @Transactional
    public Long getActionCountsByCompositeKey(String barcode, LogAction action, QueryTerm queryTerm) {
        Optional<LogAggregationEntity> entity = logAggregationRepository.findByBarcodeAndQueryTermAndAction(
                barcode, queryTerm, action);

        if (!entity.isPresent())
            return 0L;

        TimeUnit timeUnit = queryTerm.getTimeUnit();

        long queryTermStart = Instant.now()
                .minus(queryTerm.getAmountToSubtract(), chronoUnitFactory.get(timeUnit))
                .toEpochMilli();

        if (entity.get().getLastModifiedDate() < queryTermStart)
            return 0L;

        return entity.get().getActionCount();
    }
}
