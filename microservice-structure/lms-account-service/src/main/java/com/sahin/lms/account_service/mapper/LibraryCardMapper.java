package com.sahin.lms.account_service.mapper;

import com.sahin.lms.infra_authorization.model.LibraryCard;
import com.sahin.lms.infra_entity.account.jpa.LibraryCardEntity;
import com.sahin.lms.infra_mapper.CyclePreventiveContext;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AccountLoginTypeMapper.class)
public interface LibraryCardMapper {
    LibraryCard toModel(LibraryCardEntity entity, @Context CyclePreventiveContext context);
    LibraryCardEntity toEntity(LibraryCard model, @Context CyclePreventiveContext context);
}
