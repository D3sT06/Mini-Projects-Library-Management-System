package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.BookReservingEntity;
import com.sahin.library_management.infra.model.book.BookReserving;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BookItemMapper.class, MemberMapper.class})
public interface BookReservingMapper {
    BookReserving toModel(BookReservingEntity entity);
    BookReservingEntity toEntity(BookReserving model);
}