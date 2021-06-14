package com.sahin.lms.account_service.mapper;

import com.sahin.lms.infra.entity.jpa.LibraryCardEntity;
import com.sahin.lms.infra.mapper.CyclePreventiveContext;
import com.sahin.lms.infra.model.account.LibraryCard;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AccountLoginTypeMapper.class)
public interface LibraryCardMapper {
    LibraryCard toModel(LibraryCardEntity entity, @Context CyclePreventiveContext context);
    LibraryCardEntity toEntity(LibraryCard model, @Context CyclePreventiveContext context);
}
