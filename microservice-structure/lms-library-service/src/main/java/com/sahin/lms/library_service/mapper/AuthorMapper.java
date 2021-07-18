package com.sahin.lms.library_service.mapper;

import com.sahin.lms.infra_entity.library.jpa.AuthorEntity;
import com.sahin.lms.infra_mapper.CyclePreventiveContext;
import com.sahin.lms.infra_model.library.model.Author;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface AuthorMapper {
    Author toModel(AuthorEntity entity, @Context CyclePreventiveContext context);
    AuthorEntity toEntity(Author model, @Context CyclePreventiveContext context);
}

