package com.sahin.lms.infra.mapper;

import com.sahin.lms.infra.entity.jpa.BookCategoryEntity;
import com.sahin.lms.infra.entity.jpa.BookEntity;
import com.sahin.lms.infra.model.book.Book;
import com.sahin.lms.infra.model.book.BookCategory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-06-27T15:17:23+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class BookMapperImpl implements BookMapper {

    @Autowired
    private BookCategoryMapper bookCategoryMapper;
    @Autowired
    private AuthorMapper authorMapper;

    @Override
    public Book toModel(BookEntity entity, CyclePreventiveContext context) {
        Book target = context.getMappedInstance( entity, Book.class );
        if ( target != null ) {
            return target;
        }

        if ( entity == null ) {
            return null;
        }

        Book book = new Book();

        context.storeMappedInstance( entity, book );

        book.setId( entity.getId() );
        book.setTitle( entity.getTitle() );
        book.setPublicationDate( entity.getPublicationDate() );
        book.setCategories( bookCategoryMapper.toModelsSet( entity.getCategories(), context ) );
        book.setAuthor( authorMapper.toModel( entity.getAuthor(), context ) );

        return book;
    }

    @Override
    public BookEntity toEntity(Book model, CyclePreventiveContext context) {
        BookEntity target = context.getMappedInstance( model, BookEntity.class );
        if ( target != null ) {
            return target;
        }

        if ( model == null ) {
            return null;
        }

        BookEntity bookEntity = new BookEntity();

        context.storeMappedInstance( model, bookEntity );

        bookEntity.setId( model.getId() );
        bookEntity.setTitle( model.getTitle() );
        bookEntity.setPublicationDate( model.getPublicationDate() );
        bookEntity.setCategories( bookCategorySetToBookCategoryEntitySet( model.getCategories(), context ) );
        bookEntity.setAuthor( authorMapper.toEntity( model.getAuthor(), context ) );

        return bookEntity;
    }

    @Override
    public List<Book> toModels(List<BookEntity> entities, CyclePreventiveContext context) {
        List<Book> target = context.getMappedInstance( entities, List.class );
        if ( target != null ) {
            return target;
        }

        if ( entities == null ) {
            return null;
        }

        List<Book> list = new ArrayList<Book>( entities.size() );
        context.storeMappedInstance( entities, list );

        for ( BookEntity bookEntity : entities ) {
            list.add( toModel( bookEntity, context ) );
        }

        return list;
    }

    protected Set<BookCategoryEntity> bookCategorySetToBookCategoryEntitySet(Set<BookCategory> set, CyclePreventiveContext context) {
        Set<BookCategoryEntity> target = context.getMappedInstance( set, Set.class );
        if ( target != null ) {
            return target;
        }

        if ( set == null ) {
            return null;
        }

        Set<BookCategoryEntity> set1 = new HashSet<BookCategoryEntity>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        context.storeMappedInstance( set, set1 );

        for ( BookCategory bookCategory : set ) {
            set1.add( bookCategoryMapper.toEntity( bookCategory, context ) );
        }

        return set1;
    }
}
