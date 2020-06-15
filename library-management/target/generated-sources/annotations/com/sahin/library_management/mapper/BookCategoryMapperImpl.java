package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity_model.BookCategoryEntity;
import com.sahin.library_management.infra.model.book.BookCategory;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-06-05T16:58:02+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class BookCategoryMapperImpl implements BookCategoryMapper {

    @Override
    public BookCategory toModel(BookCategoryEntity entity) {
        if ( entity == null ) {
            return null;
        }

        BookCategory bookCategory = new BookCategory();

        bookCategory.setId( entity.getId() );
        bookCategory.setName( entity.getName() );

        return bookCategory;
    }

    @Override
    public BookCategoryEntity toEntity(BookCategory model) {
        if ( model == null ) {
            return null;
        }

        BookCategoryEntity bookCategoryEntity = new BookCategoryEntity();

        bookCategoryEntity.setId( model.getId() );
        bookCategoryEntity.setName( model.getName() );

        return bookCategoryEntity;
    }
}
