package com.sahin.lms.infra.mapper;

import com.sahin.lms.infra.entity.library.jpa.BookCategoryEntity;
import com.sahin.lms.infra.model.book.BookCategory;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface BookCategoryMapper {
    BookCategory toModel(BookCategoryEntity entity, @Context CyclePreventiveContext context);
    BookCategoryEntity toEntity(BookCategory model, @Context CyclePreventiveContext context);

    Set<BookCategory> toModelsSet(Set<BookCategoryEntity> entities, @Context CyclePreventiveContext context);
}