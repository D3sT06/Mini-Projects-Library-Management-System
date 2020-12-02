package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.LogAggregationEntity;
import com.sahin.library_management.infra.model.log.LogAggregation;
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
