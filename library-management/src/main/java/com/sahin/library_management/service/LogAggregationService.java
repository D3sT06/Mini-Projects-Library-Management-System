package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.LogAggregationEntity;
import com.sahin.library_management.repository.LogAggregationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class LogAggregationService {

    @Autowired
    private LogAggregationRepository logAggregationRepository;

    @Transactional
    public void saveAll(Collection<LogAggregationEntity> logs) {
        logAggregationRepository.saveAll(logs);
    }
}
