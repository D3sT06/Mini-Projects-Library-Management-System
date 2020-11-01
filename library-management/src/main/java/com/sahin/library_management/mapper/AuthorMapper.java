package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.AuthorEntity;
import com.sahin.library_management.infra.model.book.Author;
import com.sahin.library_management.infra.projections.AuthorProjections;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface AuthorMapper {
    Author toModel(AuthorEntity entity, @Context CyclePreventiveContext context);
    AuthorEntity toEntity(Author model, @Context CyclePreventiveContext context);

    AuthorProjections.AuthorView toView(AuthorEntity entity, @Context CyclePreventiveContext context);
}

