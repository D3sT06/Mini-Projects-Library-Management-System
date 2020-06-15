package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity_model.RackEntity;
import com.sahin.library_management.infra.model.book.Rack;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-06-05T16:58:02+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class RackMapperImpl implements RackMapper {

    @Override
    public Rack toModel(RackEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Rack rack = new Rack();

        rack.setId( entity.getId() );
        rack.setLocation( entity.getLocation() );

        return rack;
    }

    @Override
    public RackEntity toEntity(Rack model) {
        if ( model == null ) {
            return null;
        }

        RackEntity rackEntity = new RackEntity();

        rackEntity.setId( model.getId() );
        rackEntity.setLocation( model.getLocation() );

        return rackEntity;
    }
}
