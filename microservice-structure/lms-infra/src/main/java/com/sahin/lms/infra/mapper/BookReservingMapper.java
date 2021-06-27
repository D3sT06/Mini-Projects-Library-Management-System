package com.sahin.lms.infra.mapper;

import com.sahin.lms.infra.entity.jpa.BookReservingEntity;
import com.sahin.lms.infra.model.book.BookReserving;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BookItemMapper.class, AccountMapper.class})
@DecoratedWith(BookReservingMapperDecorator.class)
public interface BookReservingMapper {

    @Mapping(target = "bookItem", ignore = true)
    @Mapping(target = "member", ignore = true)
    BookReserving toModel(BookReservingEntity entity);

    @Mapping(target = "bookItem", ignore = true)
    @Mapping(target = "member", ignore = true)
    BookReservingEntity toEntity(BookReserving model);
}