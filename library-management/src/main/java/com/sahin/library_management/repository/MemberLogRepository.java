package com.sahin.library_management.repository;

import com.sahin.library_management.infra.model.log.MemberLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberLogRepository extends MongoRepository<MemberLog, String> {
}
