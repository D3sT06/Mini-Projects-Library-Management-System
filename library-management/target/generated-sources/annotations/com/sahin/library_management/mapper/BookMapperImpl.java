package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity_model.BookEntity;
import com.sahin.library_management.infra.model.book.Book;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-06-05T16:58:02+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class BookMapperImpl implements BookMapper {

    @Autowired
    private BookCategoryMapper bookCategoryMapper;
    @Autowired
    private AuthorMapper authorMapper;

    @Override
    public Book toModel(BookEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Book book = new Book();

        book.setId( entity.getId() );
        book.setTitle( entity.getTitle() );
        book.setPublicationDate( entity.getPublicationDate() );
        book.setCategory( bookCategoryMapper.toModel( entity.getCategory() ) );
        book.setAuthor( authorMapper.toModel( entity.getAuthor() ) );

        return book;
    }

    @Override
    public BookEntity toEntity(Book model) {
        if ( model == null ) {
            return null;
        }

        BookEntity bookEntity = new BookEntity();

        bookEntity.setId( model.getId() );
        bookEntity.setTitle( model.getTitle() );
        bookEntity.setPublicationDate( model.getPublicationDate() );
        bookEntity.setCategory( bookCategoryMapper.toEntity( model.getCategory() ) );
        bookEntity.setAuthor( authorMapper.toEntity( model.getAuthor() ) );

        return bookEntity;
    }

    @Override
    public List<Book> toModels(List<BookEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<Book> list = new ArrayList<Book>( entities.size() );
        for ( BookEntity bookEntity : entities ) {
            list.add( toModel( bookEntity ) );
        }

        return list;
    }
}
