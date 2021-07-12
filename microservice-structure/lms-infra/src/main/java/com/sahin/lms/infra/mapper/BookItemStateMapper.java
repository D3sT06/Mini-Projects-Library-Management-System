package com.sahin.lms.infra.mapper;

import com.sahin.lms.infra.entity.loan.jpa.BookItemStateEntity;
import com.sahin.lms.infra.model.book.BookItemState;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookItemStateMapper {

    @Mapping(source = "itemBarcode", target = "barcode")
    BookItemState toModel(BookItemStateEntity entity, @Context CyclePreventiveContext context);

    @Mapping(source = "barcode", target = "itemBarcode")
    BookItemStateEntity toEntity(BookItemState model, @Context CyclePreventiveContext context);
}
