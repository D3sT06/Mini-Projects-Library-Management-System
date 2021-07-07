package com.sahin.lms.infra.repository;

import com.sahin.lms.infra.entity.account.redis.TokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<TokenEntity, String> {
}

