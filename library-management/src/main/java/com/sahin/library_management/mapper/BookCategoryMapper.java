package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.BookCategoryEntity;
import com.sahin.library_management.infra.model.book.BookCategory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookCategoryMapper {
    BookCategory toModel(BookCategoryEntity entity);
    BookCategoryEntity toEntity(BookCategory model);

    List<BookCategory> toModels(List<BookCategoryEntity> entities);
}
