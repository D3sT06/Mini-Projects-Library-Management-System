package com.sahin.lms.library_service.mapper;

import com.sahin.lms.infra_entity.library.jpa.RackEntity;
import com.sahin.lms.infra_mapper.CyclePreventiveContext;
import com.sahin.lms.infra_model.library.model.Rack;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RackMapper {
    Rack toModel(RackEntity entity, @Context CyclePreventiveContext context);
    RackEntity toEntity(Rack model, @Context CyclePreventiveContext context);

    List<Rack> toModels(List<RackEntity> entities, @Context CyclePreventiveContext context);
}

