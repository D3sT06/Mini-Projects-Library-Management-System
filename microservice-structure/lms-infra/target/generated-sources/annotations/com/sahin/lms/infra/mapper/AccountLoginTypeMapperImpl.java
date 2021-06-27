package com.sahin.lms.infra.mapper;

import com.sahin.lms.infra.entity.jpa.AccountLoginTypeEntity;
import com.sahin.lms.infra.model.account.AccountLoginType;
import java.util.HashSet;
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
public class AccountLoginTypeMapperImpl implements AccountLoginTypeMapper {

    @Autowired
    private LibraryCardMapper libraryCardMapper;

    @Override
    public AccountLoginType toModel(AccountLoginTypeEntity entity, CyclePreventiveContext context) {
        AccountLoginType target = context.getMappedInstance( entity, AccountLoginType.class );
        if ( target != null ) {
            return target;
        }

        if ( entity == null ) {
            return null;
        }

        AccountLoginType accountLoginType = new AccountLoginType();

        context.storeMappedInstance( entity, accountLoginType );

        accountLoginType.setId( entity.getId() );
        accountLoginType.setLibraryCard( libraryCardMapper.toModel( entity.getLibraryCard(), context ) );
        accountLoginType.setType( entity.getType() );
        accountLoginType.setTypeSpecificKey( entity.getTypeSpecificKey() );

        return accountLoginType;
    }

    @Override
    public AccountLoginTypeEntity toEntity(AccountLoginType model, CyclePreventiveContext context) {
        AccountLoginTypeEntity target = context.getMappedInstance( model, AccountLoginTypeEntity.class );
        if ( target != null ) {
            return target;
        }

        if ( model == null ) {
            return null;
        }

        AccountLoginTypeEntity accountLoginTypeEntity = new AccountLoginTypeEntity();

        context.storeMappedInstance( model, accountLoginTypeEntity );

        accountLoginTypeEntity.setId( model.getId() );
        accountLoginTypeEntity.setLibraryCard( libraryCardMapper.toEntity( model.getLibraryCard(), context ) );
        accountLoginTypeEntity.setType( model.getType() );
        accountLoginTypeEntity.setTypeSpecificKey( model.getTypeSpecificKey() );

        return accountLoginTypeEntity;
    }

    @Override
    public Set<AccountLoginType> toModelSet(Set<AccountLoginTypeEntity> entities, CyclePreventiveContext context) {
        Set<AccountLoginType> target = context.getMappedInstance( entities, Set.class );
        if ( target != null ) {
            return target;
        }

        if ( entities == null ) {
            return null;
        }

        Set<AccountLoginType> set = new HashSet<AccountLoginType>( Math.max( (int) ( entities.size() / .75f ) + 1, 16 ) );
        context.storeMappedInstance( entities, set );

        for ( AccountLoginTypeEntity accountLoginTypeEntity : entities ) {
            set.add( toModel( accountLoginTypeEntity, context ) );
        }

        return set;
    }

    @Override
    public Set<AccountLoginTypeEntity> toEntitySet(Set<AccountLoginType> models, CyclePreventiveContext context) {
        Set<AccountLoginTypeEntity> target = context.getMappedInstance( models, Set.class );
        if ( target != null ) {
            return target;
        }

        if ( models == null ) {
            return null;
        }

        Set<AccountLoginTypeEntity> set = new HashSet<AccountLoginTypeEntity>( Math.max( (int) ( models.size() / .75f ) + 1, 16 ) );
        context.storeMappedInstance( models, set );

        for ( AccountLoginType accountLoginType : models ) {
            set.add( toEntity( accountLoginType, context ) );
        }

        return set;
    }
}
