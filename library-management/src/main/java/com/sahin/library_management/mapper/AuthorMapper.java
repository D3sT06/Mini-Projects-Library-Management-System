package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.jpa.AuthorEntity;
import com.sahin.library_management.infra.model.book.Author;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface AuthorMapper {
    Author toModel(AuthorEntity entity, @Context CyclePreventiveContext context);
    AuthorEntity toEntity(Author model, @Context CyclePreventiveContext context);
}

