package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.jpa.BookCategoryEntity;
import com.sahin.library_management.infra.model.book.BookCategory;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-06-27T15:17:12+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class BookCategoryMapperImpl implements BookCategoryMapper {

    @Override
    public BookCategory toModel(BookCategoryEntity entity, CyclePreventiveContext context) {
        BookCategory target = context.getMappedInstance( entity, BookCategory.class );
        if ( target != null ) {
            return target;
        }

        if ( entity == null ) {
            return null;
        }

        BookCategory bookCategory = new BookCategory();

        context.storeMappedInstance( entity, bookCategory );

        bookCategory.setId( entity.getId() );
        bookCategory.setName( entity.getName() );

        return bookCategory;
    }

    @Override
    public BookCategoryEntity toEntity(BookCategory model, CyclePreventiveContext context) {
        BookCategoryEntity target = context.getMappedInstance( model, BookCategoryEntity.class );
        if ( target != null ) {
            return target;
        }

        if ( model == null ) {
            return null;
        }

        BookCategoryEntity bookCategoryEntity = new BookCategoryEntity();

        context.storeMappedInstance( model, bookCategoryEntity );

        bookCategoryEntity.setId( model.getId() );
        bookCategoryEntity.setName( model.getName() );

        return bookCategoryEntity;
    }

    @Override
    public Set<BookCategory> toModelsSet(Set<BookCategoryEntity> entities, CyclePreventiveContext context) {
        Set<BookCategory> target = context.getMappedInstance( entities, Set.class );
        if ( target != null ) {
            return target;
        }

        if ( entities == null ) {
            return null;
        }

        Set<BookCategory> set = new HashSet<BookCategory>( Math.max( (int) ( entities.size() / .75f ) + 1, 16 ) );
        context.storeMappedInstance( entities, set );

        for ( BookCategoryEntity bookCategoryEntity : entities ) {
            set.add( toModel( bookCategoryEntity, context ) );
        }

        return set;
    }
}
