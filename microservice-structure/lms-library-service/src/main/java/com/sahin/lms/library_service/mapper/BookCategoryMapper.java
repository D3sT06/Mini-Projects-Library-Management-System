package com.sahin.lms.library_service.mapper;

import com.sahin.lms.infra_entity.library.jpa.BookCategoryEntity;
import com.sahin.lms.infra_mapper.CyclePreventiveContext;
import com.sahin.lms.infra_model.library.model.BookCategory;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface BookCategoryMapper {
    BookCategory toModel(BookCategoryEntity entity, @Context CyclePreventiveContext context);
    BookCategoryEntity toEntity(BookCategory model, @Context CyclePreventiveContext context);

    Set<BookCategory> toModelsSet(Set<BookCategoryEntity> entities, @Context CyclePreventiveContext context);
}
