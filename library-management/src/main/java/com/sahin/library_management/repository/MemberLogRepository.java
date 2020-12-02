package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity.MemberLogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberLogRepository extends MongoRepository<MemberLogEntity, String> {
}
