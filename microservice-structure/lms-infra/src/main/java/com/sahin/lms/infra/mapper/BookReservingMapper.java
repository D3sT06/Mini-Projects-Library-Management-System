package com.sahin.lms.infra.mapper;

import com.sahin.lms.infra.entity.loan.jpa.BookReservingEntity;
import com.sahin.lms.infra.model.book.BookReserving;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BookItemStateMapper.class})
@DecoratedWith(BookReservingMapperDecorator.class)
public interface BookReservingMapper {

    @Mapping(target = "bookItemState", ignore = true)
    BookReserving toModel(BookReservingEntity entity);

    @Mapping(target = "bookItemState", ignore = true)
    BookReservingEntity toEntity(BookReserving model);
}