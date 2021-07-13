package com.sahin.lms.log_service.service;

import com.sahin.lms.infra.entity.log.document.MemberLogEntity;
import com.sahin.lms.infra.enums.QueryTerm;
import com.sahin.lms.infra.enums.TimeUnit;
import com.sahin.lms.infra.factory.ChronoUnitFactory;
import com.sahin.lms.infra.mapper.MemberLogMapper;
import com.sahin.lms.infra.model.log.MemberLog;
import com.sahin.lms.infra.model.log.MemberLogAggregation;
import com.sahin.lms.infra.model.log.MemberLogWithBarcodeAggregation;
import com.sahin.lms.log_service.repository.MemberLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collection;
import java.util.List;


@Service
public class MemberLogService {

    private static final String ACTION_TIME = "actionTime";
    private static final String CARD_BARCODE = "cardBarcode";
    private static final String ACTION = "action";
    private static final String ACTION_COUNT = "actionCount";
    private static final String COLLECTION_NAME = "member_logs";

    @Autowired
    private MemberLogRepository memberLogRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MemberLogMapper memberLogMapper;

    @Autowired
    private ChronoUnitFactory chronoUnitFactory;

    @Transactional
    public void saveAll(Collection<MemberLog> logs) {

        Collection<MemberLogEntity> entities = memberLogMapper.toEntityCollection(logs);
        memberLogRepository.saveAll(entities);
    }

    @Transactional
    public List<MemberLog> getAll() {
        List<MemberLogEntity> entities = memberLogRepository.findAll();
        return memberLogMapper.toModelList(entities);
    }

    @Transactional
    public List<MemberLogAggregation> getActionAggregationsByBarcode(String barcode, TimeUnit timeUnit, Long amountToSubsctract) {

        MatchOperation filterTimeAndBarcode = Aggregation.match(
                Criteria.where(ACTION_TIME)
                        .gte(Instant.now().minus(amountToSubsctract, chronoUnitFactory.get(timeUnit)).toEpochMilli())
                        .and(CARD_BARCODE).is(barcode)
        );

        GroupOperation groupByAction = Aggregation.group(ACTION)
                .count().as(ACTION_COUNT);

        SortOperation sortByAction = Aggregation.sort(Sort.by(Sort.Direction.DESC, ACTION_COUNT));

        Aggregation aggregation = Aggregation.newAggregation(filterTimeAndBarcode, groupByAction, sortByAction);

        AggregationResults<MemberLogAggregation> results = mongoTemplate.aggregate(aggregation, COLLECTION_NAME, MemberLogAggregation.class);
        return results.getMappedResults();
    }

    @Transactional
    public List<MemberLogWithBarcodeAggregation> getActionAggregations(QueryTerm queryTerm) {

        MatchOperation filterTime = Aggregation.match(
                Criteria.where(ACTION_TIME)
                        .gte(Instant.now().minus(queryTerm.getAmountToSubtract(), chronoUnitFactory.get(queryTerm.getTimeUnit())).toEpochMilli())
        );

        GroupOperation groupByAction = Aggregation.group(ACTION, CARD_BARCODE)
                .count().as(ACTION_COUNT);

        SortOperation sortByAction = Aggregation.sort(Sort.by(Sort.Direction.DESC, ACTION_COUNT));

        Aggregation aggregation = Aggregation.newAggregation(filterTime, groupByAction, sortByAction);

        AggregationResults<MemberLogWithBarcodeAggregation> results = mongoTemplate.aggregate(aggregation, COLLECTION_NAME, MemberLogWithBarcodeAggregation.class);
        return results.getMappedResults();
    }
}
