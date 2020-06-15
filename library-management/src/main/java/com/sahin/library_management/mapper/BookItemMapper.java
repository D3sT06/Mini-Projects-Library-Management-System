package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity_model.BookItemEntity;
import com.sahin.library_management.infra.model.book.BookItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BookMapper.class, RackMapper.class})
public interface BookItemMapper {
    BookItem toModel(BookItemEntity entity);
    BookItemEntity toEntity(BookItem model);
}