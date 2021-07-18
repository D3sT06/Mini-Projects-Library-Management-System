package com.sahin.lms.log_service.service;

import com.sahin.lms.infra_entity.log.jpa.LogAggregationEntity;
import com.sahin.lms.infra_enum.LogAction;
import com.sahin.lms.infra_enum.QueryTerm;
import com.sahin.lms.infra_enum.TimeUnit;
import com.sahin.lms.infra_model.log.LogAggregation;
import com.sahin.lms.log_service.factory.ChronoUnitFactory;
import com.sahin.lms.log_service.mapper.LogAggregationMapper;
import com.sahin.lms.log_service.repository.LogAggregationRepository;
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
