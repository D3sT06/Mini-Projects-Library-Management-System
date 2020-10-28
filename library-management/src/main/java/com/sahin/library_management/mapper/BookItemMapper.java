package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.BookItemEntity;
import com.sahin.library_management.infra.model.book.BookItem;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BookMapper.class, RackMapper.class})
public interface BookItemMapper {
    BookItem toModel(BookItemEntity entity, @Context CyclePreventiveContext context);
    BookItemEntity toEntity(BookItem model, @Context CyclePreventiveContext context);

    List<BookItem> toModels(List<BookItemEntity> entities, @Context CyclePreventiveContext context);
}