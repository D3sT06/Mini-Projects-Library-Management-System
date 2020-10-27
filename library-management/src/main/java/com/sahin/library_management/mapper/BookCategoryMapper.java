package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.BookCategoryEntity;
import com.sahin.library_management.infra.model.book.BookCategory;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface BookCategoryMapper {
    BookCategory toModel(BookCategoryEntity entity, @Context CyclePreventiveContext context);
    BookCategoryEntity toEntity(BookCategory model, @Context CyclePreventiveContext context);

    List<BookCategory> toModels(List<BookCategoryEntity> entities, @Context CyclePreventiveContext context);
    Set<BookCategory> toModelsSet(Set<BookCategoryEntity> entities, @Context CyclePreventiveContext context);
}
