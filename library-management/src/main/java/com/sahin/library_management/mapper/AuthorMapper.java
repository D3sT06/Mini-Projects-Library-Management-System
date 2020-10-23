package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.AuthorEntity;
import com.sahin.library_management.infra.model.book.Author;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    Author toModel(AuthorEntity entity);
    AuthorEntity toEntity(Author model);

    List<Author> toModels(List<AuthorEntity> entities);
}

