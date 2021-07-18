package com.sahin.lms.loan_service.mapper;

import com.sahin.lms.infra_entity.loan.jpa.BookItemStateEntity;
import com.sahin.lms.infra_mapper.CyclePreventiveContext;
import com.sahin.lms.infra_model.loan.BookItemState;
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
