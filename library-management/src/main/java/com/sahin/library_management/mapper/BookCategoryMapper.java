package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity_model.BookCategoryEntity;
import com.sahin.library_management.infra.model.book.BookCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookCategoryMapper {
    BookCategory toModel(BookCategoryEntity entity);
    BookCategoryEntity toEntity(BookCategory model);
}