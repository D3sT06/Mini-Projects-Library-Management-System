package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.jpa.LibraryCardEntity;
import com.sahin.library_management.infra.model.account.LibraryCard;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AccountLoginTypeMapper.class)
public interface LibraryCardMapper {
    LibraryCard toModel(LibraryCardEntity entity, @Context CyclePreventiveContext context);
    LibraryCardEntity toEntity(LibraryCard model, @Context CyclePreventiveContext context);
}
