package com.sahin.lms.infra.mapper;

import com.sahin.lms.infra.entity.jpa.LibraryCardEntity;
import com.sahin.lms.infra.model.account.LibraryCard;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-06-27T15:17:23+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class LibraryCardMapperImpl implements LibraryCardMapper {

    @Autowired
    private AccountLoginTypeMapper accountLoginTypeMapper;

    @Override
    public LibraryCard toModel(LibraryCardEntity entity, CyclePreventiveContext context) {
        LibraryCard target = context.getMappedInstance( entity, LibraryCard.class );
        if ( target != null ) {
            return target;
        }

        if ( entity == null ) {
            return null;
        }

        LibraryCard libraryCard = new LibraryCard();

        context.storeMappedInstance( entity, libraryCard );

        libraryCard.setBarcode( entity.getBarcode() );
        libraryCard.setPassword( entity.getPassword() );
        libraryCard.setIssuedAt( entity.getIssuedAt() );
        libraryCard.setActive( entity.getActive() );
        libraryCard.setAccountFor( entity.getAccountFor() );
        libraryCard.setLoginTypes( accountLoginTypeMapper.toModelSet( entity.getLoginTypes(), context ) );

        return libraryCard;
    }

    @Override
    public LibraryCardEntity toEntity(LibraryCard model, CyclePreventiveContext context) {
        LibraryCardEntity target = context.getMappedInstance( model, LibraryCardEntity.class );
        if ( target != null ) {
            return target;
        }

        if ( model == null ) {
            return null;
        }

        LibraryCardEntity libraryCardEntity = new LibraryCardEntity();

        context.storeMappedInstance( model, libraryCardEntity );

        libraryCardEntity.setBarcode( model.getBarcode() );
        libraryCardEntity.setIssuedAt( model.getIssuedAt() );
        libraryCardEntity.setPassword( model.getPassword() );
        libraryCardEntity.setActive( model.getActive() );
        libraryCardEntity.setAccountFor( model.getAccountFor() );
        libraryCardEntity.setLoginTypes( accountLoginTypeMapper.toEntitySet( model.getLoginTypes(), context ) );

        return libraryCardEntity;
    }
}
