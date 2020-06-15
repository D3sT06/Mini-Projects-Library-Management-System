package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity_model.AuthorEntity;
import com.sahin.library_management.infra.model.book.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    Author toModel(AuthorEntity entity);
    AuthorEntity toEntity(Author model);
}
