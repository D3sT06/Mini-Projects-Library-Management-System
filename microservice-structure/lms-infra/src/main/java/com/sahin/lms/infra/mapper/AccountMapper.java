package com.sahin.lms.infra.mapper;

import com.sahin.lms.infra.entity.account.jpa.AccountEntity;
import com.sahin.lms.infra.enums.AccountFor;
import com.sahin.lms.infra.model.account.Account;
import com.sahin.lms.infra.model.account.Librarian;
import com.sahin.lms.infra.model.account.Member;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {LibraryCardMapper.class})
public interface AccountMapper {

    default Account toModel(@MappingTarget AccountEntity entity) {
        if (entity.getLibraryCard().getAccountFor().equals(AccountFor.LIBRARIAN))
            return toLibrarianModel(entity, new CyclePreventiveContext());
        else if (entity.getLibraryCard().getAccountFor().equals(AccountFor.MEMBER))
            return toMemberModel(entity, new CyclePreventiveContext());
        else
            throw new IllegalStateException("Not a valid account entity");
    }

    default List<Member> toMemberModels(@MappingTarget List<AccountEntity> entities) {
        List<Member> members = new ArrayList<>();

        for (AccountEntity entity : entities)
            if (entity.getLibraryCard().getAccountFor().equals(AccountFor.MEMBER))
                members.add(toMemberModel(entity, new CyclePreventiveContext()));
            else
                throw new IllegalStateException("Not a valid member entity");

        return members;
    }

    default List<Librarian> toLibrarianModels(@MappingTarget List<AccountEntity> entities) {
        List<Librarian> librarians = new ArrayList<>();

        for (AccountEntity entity : entities)
            if (entity.getLibraryCard().getAccountFor().equals(AccountFor.LIBRARIAN))
                librarians.add(toLibrarianModel(entity, new CyclePreventiveContext()));
            else
                throw new IllegalStateException("Not a valid librarian entity");

        return librarians;
    }

    @Mapping(source = "libraryCard.accountFor", target = "type")
    AccountEntity toEntity(Account model, @Context CyclePreventiveContext context);

    Librarian toLibrarianModel(AccountEntity entity, @Context CyclePreventiveContext context);
    Member toMemberModel(AccountEntity entity, @Context CyclePreventiveContext context);
}