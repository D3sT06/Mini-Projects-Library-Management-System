package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.document.MemberLogEntity;
import com.sahin.library_management.infra.model.log.MemberLog;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-06-27T15:17:12+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class MemberLogMapperImpl implements MemberLogMapper {

    @Override
    public MemberLog toModel(MemberLogEntity memberLog) {
        if ( memberLog == null ) {
            return null;
        }

        MemberLog memberLog1 = new MemberLog();

        memberLog1.setId( memberLog.getId() );
        memberLog1.setCardBarcode( memberLog.getCardBarcode() );
        memberLog1.setActionTime( memberLog.getActionTime() );
        memberLog1.setAction( memberLog.getAction() );
        memberLog1.setMessage( memberLog.getMessage() );
        memberLog1.setDetails( memberLog.getDetails() );
        memberLog1.setHttpStatus( memberLog.getHttpStatus() );

        return memberLog1;
    }

    @Override
    public List<MemberLog> toModelList(List<MemberLogEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<MemberLog> list = new ArrayList<MemberLog>( entities.size() );
        for ( MemberLogEntity memberLogEntity : entities ) {
            list.add( toModel( memberLogEntity ) );
        }

        return list;
    }

    @Override
    public MemberLogEntity toEntity(MemberLog memberLog) {
        if ( memberLog == null ) {
            return null;
        }

        MemberLogEntity memberLogEntity = new MemberLogEntity();

        memberLogEntity.setId( memberLog.getId() );
        memberLogEntity.setCardBarcode( memberLog.getCardBarcode() );
        memberLogEntity.setActionTime( memberLog.getActionTime() );
        memberLogEntity.setAction( memberLog.getAction() );
        memberLogEntity.setMessage( memberLog.getMessage() );
        memberLogEntity.setDetails( memberLog.getDetails() );
        memberLogEntity.setHttpStatus( memberLog.getHttpStatus() );

        return memberLogEntity;
    }

    @Override
    public Collection<MemberLogEntity> toEntityCollection(Collection<MemberLog> logs) {
        if ( logs == null ) {
            return null;
        }

        Collection<MemberLogEntity> collection = new ArrayList<MemberLogEntity>( logs.size() );
        for ( MemberLog memberLog : logs ) {
            collection.add( toEntity( memberLog ) );
        }

        return collection;
    }
}
