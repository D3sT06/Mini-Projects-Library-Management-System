package com.sahin.lms.loan_service.mapper;

import com.sahin.lms.infra_entity.loan.jpa.BookReservingEntity;
import com.sahin.lms.infra_model.loan.BookReserving;
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