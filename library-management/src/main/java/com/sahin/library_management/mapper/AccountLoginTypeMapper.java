package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.jpa.AccountLoginTypeEntity;
import com.sahin.library_management.infra.model.account.AccountLoginType;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring", uses = LibraryCardMapper.class)
public interface AccountLoginTypeMapper {
    AccountLoginType toModel(AccountLoginTypeEntity entity, @Context CyclePreventiveContext context);
    AccountLoginTypeEntity toEntity(AccountLoginType model, @Context CyclePreventiveContext context);

    Set<AccountLoginType> toModelSet(Set<AccountLoginTypeEntity> entities, @Context CyclePreventiveContext context);
    Set<AccountLoginTypeEntity> toEntitySet(Set<AccountLoginType> models, @Context CyclePreventiveContext context);
}
