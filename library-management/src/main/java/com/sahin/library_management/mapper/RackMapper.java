package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.RackEntity;
import com.sahin.library_management.infra.model.book.Rack;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RackMapper {
    Rack toModel(RackEntity entity, @Context CyclePreventiveContext context);
    RackEntity toEntity(Rack model, @Context CyclePreventiveContext context);

    List<Rack> toModels(List<RackEntity> entities, @Context CyclePreventiveContext context);
}

