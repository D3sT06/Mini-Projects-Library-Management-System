package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity_model.LibraryCardEntity;
import com.sahin.library_management.infra.model.account.LibraryCard;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-06-05T16:58:02+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class LibraryCardMapperImpl implements LibraryCardMapper {

    @Override
    public LibraryCard toModel(LibraryCardEntity entity) {
        if ( entity == null ) {
            return null;
        }

        LibraryCard libraryCard = new LibraryCard();

        libraryCard.setBarcode( entity.getBarcode() );
        libraryCard.setIssuedAt( entity.getIssuedAt() );
        libraryCard.setActive( entity.getActive() );
        libraryCard.setAccountFor( entity.getAccountFor() );

        return libraryCard;
    }

    @Override
    public LibraryCardEntity toEntity(LibraryCard model) {
        if ( model == null ) {
            return null;
        }

        LibraryCardEntity libraryCardEntity = new LibraryCardEntity();

        libraryCardEntity.setBarcode( model.getBarcode() );
        libraryCardEntity.setIssuedAt( model.getIssuedAt() );
        libraryCardEntity.setActive( model.getActive() );
        libraryCardEntity.setAccountFor( model.getAccountFor() );

        return libraryCardEntity;
    }
}
