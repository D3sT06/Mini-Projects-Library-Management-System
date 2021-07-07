package com.sahin.lms.log_service.repository;

import com.sahin.lms.infra.entity.log.document.MemberLogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberLogRepository extends MongoRepository<MemberLogEntity, String> {
}
