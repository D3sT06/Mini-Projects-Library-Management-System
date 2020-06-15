package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity_model.RackEntity;
import com.sahin.library_management.infra.model.book.Rack;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RackMapper {
    Rack toModel(RackEntity entity);
    RackEntity toEntity(Rack model);
}
