package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.jpa.BookReservingEntity;
import com.sahin.library_management.infra.model.book.BookReserving;
import com.sahin.library_management.infra.model.book.BookReserving.BookReservingBuilder;
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
public class BookReservingMapperImpl_ implements BookReservingMapper {

    @Override
    public BookReserving toModel(BookReservingEntity entity) {
        if ( entity == null ) {
            return null;
        }

        BookReservingBuilder bookReserving = BookReserving.builder();

        bookReserving.id( entity.getId() );
        bookReserving.reservedAt( entity.getReservedAt() );
        bookReserving.dueDate( entity.getDueDate() );
        bookReserving.expectedLoanAt( entity.getExpectedLoanAt() );

        return bookReserving.build();
    }

    @Override
    public BookReservingEntity toEntity(BookReserving model) {
        if ( model == null ) {
            return null;
        }

        BookReservingEntity bookReservingEntity = new BookReservingEntity();

        bookReservingEntity.setId( model.getId() );
        bookReservingEntity.setReservedAt( model.getReservedAt() );
        bookReservingEntity.setDueDate( model.getDueDate() );
        bookReservingEntity.setExpectedLoanAt( model.getExpectedLoanAt() );

        return bookReservingEntity;
    }
}
