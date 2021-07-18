package com.sahin.lms.infra_authentication.repository;

import com.sahin.lms.infra_entity.account.redis.TokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<TokenEntity, String> {
}
