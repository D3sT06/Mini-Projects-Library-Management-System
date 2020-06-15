package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity_model.BookItemEntity;
import com.sahin.library_management.infra.model.book.BookItem;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-06-05T16:58:02+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class BookItemMapperImpl implements BookItemMapper {

    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private RackMapper rackMapper;

    @Override
    public BookItem toModel(BookItemEntity entity) {
        if ( entity == null ) {
            return null;
        }

        BookItem bookItem = new BookItem();

        bookItem.setBarcode( entity.getBarcode() );
        bookItem.setBook( bookMapper.toModel( entity.getBook() ) );
        bookItem.setRack( rackMapper.toModel( entity.getRack() ) );
        bookItem.setStatus( entity.getStatus() );

        return bookItem;
    }

    @Override
    public BookItemEntity toEntity(BookItem model) {
        if ( model == null ) {
            return null;
        }

        BookItemEntity bookItemEntity = new BookItemEntity();

        bookItemEntity.setBarcode( model.getBarcode() );
        bookItemEntity.setBook( bookMapper.toEntity( model.getBook() ) );
        bookItemEntity.setRack( rackMapper.toEntity( model.getRack() ) );
        bookItemEntity.setStatus( model.getStatus() );

        return bookItemEntity;
    }
}
