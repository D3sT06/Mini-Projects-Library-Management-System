package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.jpa.BookItemEntity;
import com.sahin.library_management.infra.model.book.BookItem;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-06-27T15:17:12+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class BookItemMapperImpl implements BookItemMapper {

    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private RackMapper rackMapper;

    @Override
    public BookItem toModel(BookItemEntity entity, CyclePreventiveContext context) {
        BookItem target = context.getMappedInstance( entity, BookItem.class );
        if ( target != null ) {
            return target;
        }

        if ( entity == null ) {
            return null;
        }

        BookItem bookItem = new BookItem();

        context.storeMappedInstance( entity, bookItem );

        bookItem.setBarcode( entity.getBarcode() );
        bookItem.setBook( bookMapper.toModel( entity.getBook(), context ) );
        bookItem.setRack( rackMapper.toModel( entity.getRack(), context ) );
        bookItem.setStatus( entity.getStatus() );

        return bookItem;
    }

    @Override
    public BookItemEntity toEntity(BookItem model, CyclePreventiveContext context) {
        BookItemEntity target = context.getMappedInstance( model, BookItemEntity.class );
        if ( target != null ) {
            return target;
        }

        if ( model == null ) {
            return null;
        }

        BookItemEntity bookItemEntity = new BookItemEntity();

        context.storeMappedInstance( model, bookItemEntity );

        bookItemEntity.setBarcode( model.getBarcode() );
        bookItemEntity.setBook( bookMapper.toEntity( model.getBook(), context ) );
        bookItemEntity.setRack( rackMapper.toEntity( model.getRack(), context ) );
        bookItemEntity.setStatus( model.getStatus() );

        return bookItemEntity;
    }

    @Override
    public List<BookItem> toModels(List<BookItemEntity> entities, CyclePreventiveContext context) {
        List<BookItem> target = context.getMappedInstance( entities, List.class );
        if ( target != null ) {
            return target;
        }

        if ( entities == null ) {
            return null;
        }

        List<BookItem> list = new ArrayList<BookItem>( entities.size() );
        context.storeMappedInstance( entities, list );

        for ( BookItemEntity bookItemEntity : entities ) {
            list.add( toModel( bookItemEntity, context ) );
        }

        return list;
    }
}
