package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.jpa.BookLoaningEntity;
import com.sahin.library_management.infra.model.book.BookLoaning;
import com.sahin.library_management.infra.model.book.BookLoaning.BookLoaningBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-06-27T15:17:12+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
@Qualifier("delegate")
public class BookLoaningMapperImpl_ implements BookLoaningMapper {

    @Override
    public BookLoaning toModel(BookLoaningEntity entity) {
        if ( entity == null ) {
            return null;
        }

        BookLoaningBuilder bookLoaning = BookLoaning.builder();

        bookLoaning.id( entity.getId() );
        bookLoaning.loanedAt( entity.getLoanedAt() );
        bookLoaning.dueDate( entity.getDueDate() );
        bookLoaning.returnedAt( entity.getReturnedAt() );

        return bookLoaning.build();
    }

    @Override
    public BookLoaningEntity toEntity(BookLoaning model) {
        if ( model == null ) {
            return null;
        }

        BookLoaningEntity bookLoaningEntity = new BookLoaningEntity();

        bookLoaningEntity.setId( model.getId() );
        bookLoaningEntity.setLoanedAt( model.getLoanedAt() );
        bookLoaningEntity.setDueDate( model.getDueDate() );
        bookLoaningEntity.setReturnedAt( model.getReturnedAt() );

        return bookLoaningEntity;
    }

    @Override
    public List<BookLoaning> toModels(List<BookLoaningEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<BookLoaning> list = new ArrayList<BookLoaning>( entities.size() );
        for ( BookLoaningEntity bookLoaningEntity : entities ) {
            list.add( toModel( bookLoaningEntity ) );
        }

        return list;
    }
}
