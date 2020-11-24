package com.sahin.library_management.service.member_log;

import com.sahin.library_management.infra.enums.QueryTerm;
import com.sahin.library_management.infra.enums.TimeUnit;
import com.sahin.library_management.infra.model.log.MemberLog;
import com.sahin.library_management.infra.model.log.MemberLogAggregation;
import com.sahin.library_management.infra.model.log.MemberLogWithBarcodeAggregation;
import com.sahin.library_management.repository.MemberLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.EnumMap;
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

    @Transactional
    public void saveAll(Collection<MemberLog> logs) {
        memberLogRepository.saveAll(logs);
    }

    @Transactional
    public List<MemberLog> getAll() {
        return memberLogRepository.findAll();
    }

    private EnumMap<TimeUnit, ChronoUnit> timeChronoMap;

    public MemberLogService() {
        timeChronoMap = new EnumMap<>(TimeUnit.class);
        timeChronoMap.put(TimeUnit.SECONDS, ChronoUnit.SECONDS);
        timeChronoMap.put(TimeUnit.MINUTES, ChronoUnit.SECONDS);
        timeChronoMap.put(TimeUnit.HOURS, ChronoUnit.HOURS);
        timeChronoMap.put(TimeUnit.DAYS, ChronoUnit.DAYS);
    }

    @Transactional
    public List<MemberLogAggregation> getActionAggregationsByBarcode(String barcode, TimeUnit timeUnit, Long amountToSubsctract) {

        MatchOperation filterTimeAndBarcode = Aggregation.match(
                Criteria.where(ACTION_TIME)
                        .gte(Instant.now().minus(amountToSubsctract, timeChronoMap.get(timeUnit)).toEpochMilli())
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
                        .gte(Instant.now().minus(queryTerm.getAmountToSubtract(), timeChronoMap.get(queryTerm.getTimeUnit())).toEpochMilli())
        );

        GroupOperation groupByAction = Aggregation.group(ACTION, CARD_BARCODE)
                .count().as(ACTION_COUNT);

        SortOperation sortByAction = Aggregation.sort(Sort.by(Sort.Direction.DESC, ACTION_COUNT));

        Aggregation aggregation = Aggregation.newAggregation(filterTime, groupByAction, sortByAction);

        AggregationResults<MemberLogWithBarcodeAggregation> results = mongoTemplate.aggregate(aggregation, COLLECTION_NAME, MemberLogWithBarcodeAggregation.class);
        return results.getMappedResults();
    }
}
