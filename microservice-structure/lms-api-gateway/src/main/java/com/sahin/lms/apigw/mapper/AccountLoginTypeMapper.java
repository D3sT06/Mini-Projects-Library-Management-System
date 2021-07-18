package com.sahin.lms.apigw.mapper;

import com.sahin.lms.infra_authorization.model.AccountLoginType;
import com.sahin.lms.infra_entity.account.jpa.AccountLoginTypeEntity;
import com.sahin.lms.infra_mapper.CyclePreventiveContext;
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
