package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity_model.BookLoaningEntity;
import com.sahin.library_management.infra.model.book.BookLoaning;
import com.sahin.library_management.infra.model.book.BookLoaning.BookLoaningBuilder;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-06-05T16:58:02+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class BookLoaningMapperImpl implements BookLoaningMapper {

    @Autowired
    private BookItemMapper bookItemMapper;
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public BookLoaning toModel(BookLoaningEntity entity) {
        if ( entity == null ) {
            return null;
        }

        BookLoaningBuilder bookLoaning = BookLoaning.builder();

        bookLoaning.id( entity.getId() );
        bookLoaning.bookItem( bookItemMapper.toModel( entity.getBookItem() ) );
        bookLoaning.member( memberMapper.toModel( entity.getMember() ) );
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
        bookLoaningEntity.setBookItem( bookItemMapper.toEntity( model.getBookItem() ) );
        bookLoaningEntity.setMember( memberMapper.toEntity( model.getMember() ) );
        bookLoaningEntity.setLoanedAt( model.getLoanedAt() );
        bookLoaningEntity.setDueDate( model.getDueDate() );
        bookLoaningEntity.setReturnedAt( model.getReturnedAt() );

        return bookLoaningEntity;
    }
}
