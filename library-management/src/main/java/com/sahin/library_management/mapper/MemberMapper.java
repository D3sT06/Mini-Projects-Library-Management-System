package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity_model.MemberEntity;
import com.sahin.library_management.infra.model.account.Member;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {LibraryCardMapper.class})
public interface MemberMapper {
    Member toModel(MemberEntity entity);
    MemberEntity toEntity(Member model);

    List<Member> toModels(List<MemberEntity> entities);
}
