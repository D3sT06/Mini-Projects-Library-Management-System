package com.sahin.library_management.mapper;

import com.sahin.library_management.infra.entity.MemberLogEntity;
import com.sahin.library_management.infra.model.log.MemberLog;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberLogMapper {

    MemberLog toModel(MemberLogEntity memberLog);
    List<MemberLog> toModelList(List<MemberLogEntity> entities);

    MemberLogEntity toEntity(MemberLog memberLog);
    Collection<MemberLogEntity> toEntityCollection(Collection<MemberLog> logs);
}
