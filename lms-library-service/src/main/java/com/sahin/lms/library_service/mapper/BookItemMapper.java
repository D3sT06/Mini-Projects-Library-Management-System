package com.sahin.lms.library_service.mapper;

import com.sahin.lms.infra.entity.jpa.BookItemEntity;
import com.sahin.lms.infra.mapper.CyclePreventiveContext;
import com.sahin.lms.infra.model.book.BookItem;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BookMapper.class, RackMapper.class})
public interface BookItemMapper {
    BookItem toModel(BookItemEntity entity, @Context CyclePreventiveContext context);
    BookItemEntity toEntity(BookItem model, @Context CyclePreventiveContext context);

    List<BookItem> toModels(List<BookItemEntity> entities, @Context CyclePreventiveContext context);
}