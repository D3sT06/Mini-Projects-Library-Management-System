package com.sahin.lms.infra.repository;

import com.sahin.lms.infra.entity.redis.TokenEntity;
import org.springframework.data.repository.CrudRepository;

public interface ITokenRepository extends CrudRepository<TokenEntity, String> {
}