package com.sahin.lms.infra.mapper;

import com.sahin.lms.infra.entity.log.jpa.LogAggregationEntity;
import com.sahin.lms.infra.model.log.LogAggregation;
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
