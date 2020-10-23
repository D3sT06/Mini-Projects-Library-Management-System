package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.LibrarianEntity;
import com.sahin.library_management.infra.model.account.Librarian;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {LibraryCardMapper.class})
public interface LibrarianMapper {
    Librarian toModel(LibrarianEntity entity);
    List<Librarian> toModels(List<LibrarianEntity> entities);

    LibrarianEntity toEntity(Librarian model);
}