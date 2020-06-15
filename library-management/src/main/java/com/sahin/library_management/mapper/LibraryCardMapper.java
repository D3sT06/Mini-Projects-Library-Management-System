package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity_model.LibraryCardEntity;
import com.sahin.library_management.infra.model.account.LibraryCard;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LibraryCardMapper {
    LibraryCard toModel(LibraryCardEntity entity);
    LibraryCardEntity toEntity(LibraryCard model);
}
