package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.jpa.AuthorEntity;
import com.sahin.library_management.infra.model.book.Author;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-06-27T15:17:12+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class AuthorMapperImpl implements AuthorMapper {

    @Override
    public Author toModel(AuthorEntity entity, CyclePreventiveContext context) {
        Author target = context.getMappedInstance( entity, Author.class );
        if ( target != null ) {
            return target;
        }

        if ( entity == null ) {
            return null;
        }

        Author author = new Author();

        context.storeMappedInstance( entity, author );

        author.setId( entity.getId() );
        author.setName( entity.getName() );
        author.setSurname( entity.getSurname() );

        return author;
    }

    @Override
    public AuthorEntity toEntity(Author model, CyclePreventiveContext context) {
        AuthorEntity target = context.getMappedInstance( model, AuthorEntity.class );
        if ( target != null ) {
            return target;
        }

        if ( model == null ) {
            return null;
        }

        AuthorEntity authorEntity = new AuthorEntity();

        context.storeMappedInstance( model, authorEntity );

        authorEntity.setId( model.getId() );
        authorEntity.setName( model.getName() );
        authorEntity.setSurname( model.getSurname() );

        return authorEntity;
    }
}
