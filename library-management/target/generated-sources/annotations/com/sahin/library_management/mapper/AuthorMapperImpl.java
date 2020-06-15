package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity_model.AuthorEntity;
import com.sahin.library_management.infra.model.book.Author;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-06-05T16:58:02+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class AuthorMapperImpl implements AuthorMapper {

    @Override
    public Author toModel(AuthorEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Author author = new Author();

        author.setId( entity.getId() );
        author.setName( entity.getName() );
        author.setSurname( entity.getSurname() );

        return author;
    }

    @Override
    public AuthorEntity toEntity(Author model) {
        if ( model == null ) {
            return null;
        }

        AuthorEntity authorEntity = new AuthorEntity();

        authorEntity.setId( model.getId() );
        authorEntity.setName( model.getName() );
        authorEntity.setSurname( model.getSurname() );

        return authorEntity;
    }
}
