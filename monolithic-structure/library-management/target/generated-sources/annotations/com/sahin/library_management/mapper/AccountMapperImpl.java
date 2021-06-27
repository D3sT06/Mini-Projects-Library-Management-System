package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.jpa.AccountEntity;
import com.sahin.library_management.infra.enums.AccountFor;
import com.sahin.library_management.infra.model.account.Account;
import com.sahin.library_management.infra.model.account.Librarian;
import com.sahin.library_management.infra.model.account.LibraryCard;
import com.sahin.library_management.infra.model.account.Member;
import javax.annotation.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-06-27T15:17:12+0300",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_101 (Oracle Corporation)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Autowired
    private LibraryCardMapper libraryCardMapper;

    @Override
    public AccountEntity toEntity(Account model, CyclePreventiveContext context) {
        AccountEntity target = context.getMappedInstance( model, AccountEntity.class );
        if ( target != null ) {
            return target;
        }

        if ( model == null ) {
            return null;
        }

        AccountEntity accountEntity = new AccountEntity();

        context.storeMappedInstance( model, accountEntity );

        accountEntity.setType( modelLibraryCardAccountFor( model ) );
        accountEntity.setId( model.getId() );
        accountEntity.setLibraryCard( libraryCardMapper.toEntity( model.getLibraryCard(), context ) );
        accountEntity.setName( model.getName() );
        accountEntity.setSurname( model.getSurname() );
        accountEntity.setEmail( model.getEmail() );
        accountEntity.setPhone( model.getPhone() );

        return accountEntity;
    }

    @Override
    public Librarian toLibrarianModel(AccountEntity entity, CyclePreventiveContext context) {
        Librarian target = context.getMappedInstance( entity, Librarian.class );
        if ( target != null ) {
            return target;
        }

        if ( entity == null ) {
            return null;
        }

        Librarian librarian = new Librarian();

        context.storeMappedInstance( entity, librarian );

        librarian.setId( entity.getId() );
        librarian.setLibraryCard( libraryCardMapper.toModel( entity.getLibraryCard(), context ) );
        librarian.setName( entity.getName() );
        librarian.setSurname( entity.getSurname() );
        librarian.setEmail( entity.getEmail() );
        librarian.setPhone( entity.getPhone() );

        return librarian;
    }

    @Override
    public Member toMemberModel(AccountEntity entity, CyclePreventiveContext context) {
        Member target = context.getMappedInstance( entity, Member.class );
        if ( target != null ) {
            return target;
        }

        if ( entity == null ) {
            return null;
        }

        Member member = new Member();

        context.storeMappedInstance( entity, member );

        member.setId( entity.getId() );
        member.setLibraryCard( libraryCardMapper.toModel( entity.getLibraryCard(), context ) );
        member.setName( entity.getName() );
        member.setSurname( entity.getSurname() );
        member.setEmail( entity.getEmail() );
        member.setPhone( entity.getPhone() );

        return member;
    }

    private AccountFor modelLibraryCardAccountFor(Account account) {
        if ( account == null ) {
            return null;
        }
        LibraryCard libraryCard = account.getLibraryCard();
        if ( libraryCard == null ) {
            return null;
        }
        AccountFor accountFor = libraryCard.getAccountFor();
        if ( accountFor == null ) {
            return null;
        }
        return accountFor;
    }
}
