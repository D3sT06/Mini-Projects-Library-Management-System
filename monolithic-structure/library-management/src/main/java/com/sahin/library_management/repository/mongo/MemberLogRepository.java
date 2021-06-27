package com.sahin.library_management.repository.mongo;

import com.sahin.library_management.infra.entity.document.MemberLogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberLogRepository extends MongoRepository<MemberLogEntity, String> {
}
