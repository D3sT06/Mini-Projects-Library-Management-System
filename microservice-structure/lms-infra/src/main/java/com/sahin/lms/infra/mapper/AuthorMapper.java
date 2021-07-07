package com.sahin.lms.infra.mapper;

import com.sahin.lms.infra.entity.library.jpa.AuthorEntity;
import com.sahin.lms.infra.model.book.Author;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface AuthorMapper {
    Author toModel(AuthorEntity entity, @Context CyclePreventiveContext context);
    AuthorEntity toEntity(Author model, @Context CyclePreventiveContext context);
}

