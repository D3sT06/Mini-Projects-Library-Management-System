package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity_model.LibrarianEntity;
import com.sahin.library_management.infra.model.account.Librarian;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-06-05T16:58:02+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class LibrarianMapperImpl implements LibrarianMapper {

    @Autowired
    private LibraryCardMapper libraryCardMapper;

    @Override
    public Librarian toModel(LibrarianEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Librarian librarian = new Librarian();

        librarian.setId( entity.getId() );
        librarian.setLibraryCard( libraryCardMapper.toModel( entity.getLibraryCard() ) );
        librarian.setName( entity.getName() );
        librarian.setSurname( entity.getSurname() );
        librarian.setEmail( entity.getEmail() );
        librarian.setPhone( entity.getPhone() );

        return librarian;
    }

    @Override
    public LibrarianEntity toEntity(Librarian model) {
        if ( model == null ) {
            return null;
        }

        LibrarianEntity librarianEntity = new LibrarianEntity();

        librarianEntity.setId( model.getId() );
        librarianEntity.setLibraryCard( libraryCardMapper.toEntity( model.getLibraryCard() ) );
        librarianEntity.setName( model.getName() );
        librarianEntity.setSurname( model.getSurname() );
        librarianEntity.setEmail( model.getEmail() );
        librarianEntity.setPhone( model.getPhone() );

        return librarianEntity;
    }
}
