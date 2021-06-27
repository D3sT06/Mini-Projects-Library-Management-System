package com.sahin.lms.infra.mapper;

import com.sahin.lms.infra.entity.document.MemberLogEntity;
import com.sahin.lms.infra.model.log.MemberLog;
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
