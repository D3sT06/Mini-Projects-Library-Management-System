package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.jpa.RackEntity;
import com.sahin.library_management.infra.model.book.Rack;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-06-27T15:17:12+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class RackMapperImpl implements RackMapper {

    @Override
    public Rack toModel(RackEntity entity, CyclePreventiveContext context) {
        Rack target = context.getMappedInstance( entity, Rack.class );
        if ( target != null ) {
            return target;
        }

        if ( entity == null ) {
            return null;
        }

        Rack rack = new Rack();

        context.storeMappedInstance( entity, rack );

        rack.setId( entity.getId() );
        rack.setLocation( entity.getLocation() );

        return rack;
    }

    @Override
    public RackEntity toEntity(Rack model, CyclePreventiveContext context) {
        RackEntity target = context.getMappedInstance( model, RackEntity.class );
        if ( target != null ) {
            return target;
        }

        if ( model == null ) {
            return null;
        }

        RackEntity rackEntity = new RackEntity();

        context.storeMappedInstance( model, rackEntity );

        rackEntity.setId( model.getId() );
        rackEntity.setLocation( model.getLocation() );

        return rackEntity;
    }

    @Override
    public List<Rack> toModels(List<RackEntity> entities, CyclePreventiveContext context) {
        List<Rack> target = context.getMappedInstance( entities, List.class );
        if ( target != null ) {
            return target;
        }

        if ( entities == null ) {
            return null;
        }

        List<Rack> list = new ArrayList<Rack>( entities.size() );
        context.storeMappedInstance( entities, list );

        for ( RackEntity rackEntity : entities ) {
            list.add( toModel( rackEntity, context ) );
        }

        return list;
    }
}
