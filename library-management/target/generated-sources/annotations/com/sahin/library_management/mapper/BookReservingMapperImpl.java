package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity_model.BookReservingEntity;
import com.sahin.library_management.infra.model.book.BookReserving;
import com.sahin.library_management.infra.model.book.BookReserving.BookReservingBuilder;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-06-05T16:58:02+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class BookReservingMapperImpl implements BookReservingMapper {

    @Autowired
    private BookItemMapper bookItemMapper;
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public BookReserving toModel(BookReservingEntity entity) {
        if ( entity == null ) {
            return null;
        }

        BookReservingBuilder bookReserving = BookReserving.builder();

        bookReserving.id( entity.getId() );
        bookReserving.bookItem( bookItemMapper.toModel( entity.getBookItem() ) );
        bookReserving.member( memberMapper.toModel( entity.getMember() ) );
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
        bookReservingEntity.setBookItem( bookItemMapper.toEntity( model.getBookItem() ) );
        bookReservingEntity.setMember( memberMapper.toEntity( model.getMember() ) );
        bookReservingEntity.setReservedAt( model.getReservedAt() );
        bookReservingEntity.setDueDate( model.getDueDate() );
        bookReservingEntity.setExpectedLoanAt( model.getExpectedLoanAt() );

        return bookReservingEntity;
    }
}
