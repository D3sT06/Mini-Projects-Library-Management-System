package com.sahin.library_management.repository.redis;

import com.sahin.library_management.infra.entity.redis.TokenEntity;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<TokenEntity, String> {
}
