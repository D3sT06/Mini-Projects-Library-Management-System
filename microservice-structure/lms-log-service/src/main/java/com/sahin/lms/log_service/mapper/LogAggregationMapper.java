package com.sahin.lms.log_service.mapper;

import com.sahin.lms.infra_entity.log.jpa.LogAggregationEntity;
import com.sahin.lms.infra_model.log.LogAggregation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface LogAggregationMapper {

    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate",ignore = true)
    LogAggregationEntity toEntity(LogAggregation logAggregation);

    Collection<LogAggregationEntity> toEntityCollection(Collection<LogAggregation> logAggregationCollection);

}
