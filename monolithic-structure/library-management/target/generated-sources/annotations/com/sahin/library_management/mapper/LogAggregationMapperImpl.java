package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.jpa.LogAggregationEntity;
import com.sahin.library_management.infra.model.log.LogAggregation;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-06-27T15:17:12+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class LogAggregationMapperImpl implements LogAggregationMapper {

    @Override
    public LogAggregationEntity toEntity(LogAggregation logAggregation) {
        if ( logAggregation == null ) {
            return null;
        }

        LogAggregationEntity logAggregationEntity = new LogAggregationEntity();

        logAggregationEntity.setBarcode( logAggregation.getBarcode() );
        logAggregationEntity.setAction( logAggregation.getAction() );
        logAggregationEntity.setActionCount( logAggregation.getActionCount() );
        logAggregationEntity.setAccountFor( logAggregation.getAccountFor() );
        logAggregationEntity.setQueryTerm( logAggregation.getQueryTerm() );

        return logAggregationEntity;
    }

    @Override
    public Collection<LogAggregationEntity> toEntityCollection(Collection<LogAggregation> logAggregationCollection) {
        if ( logAggregationCollection == null ) {
            return null;
        }

        Collection<LogAggregationEntity> collection = new ArrayList<LogAggregationEntity>( logAggregationCollection.size() );
        for ( LogAggregation logAggregation : logAggregationCollection ) {
            collection.add( toEntity( logAggregation ) );
        }

        return collection;
    }
}
