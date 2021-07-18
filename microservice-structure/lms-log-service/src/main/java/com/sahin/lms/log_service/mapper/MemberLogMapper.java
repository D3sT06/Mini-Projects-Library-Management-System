package com.sahin.lms.log_service.mapper;

import com.sahin.lms.infra_entity.log.document.MemberLogEntity;
import com.sahin.lms.infra_service.member_log.model.MemberLog;
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
