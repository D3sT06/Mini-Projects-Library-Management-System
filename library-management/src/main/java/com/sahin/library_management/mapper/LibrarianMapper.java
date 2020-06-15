package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity_model.LibrarianEntity;
import com.sahin.library_management.infra.model.account.Librarian;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {LibraryCardMapper.class})
public interface LibrarianMapper {
    Librarian toModel(LibrarianEntity entity);
    LibrarianEntity toEntity(Librarian model);
}