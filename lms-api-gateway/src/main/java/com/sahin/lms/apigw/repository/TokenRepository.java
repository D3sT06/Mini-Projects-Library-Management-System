package com.sahin.lms.apigw.repository;

import com.sahin.lms.infra.auth.entity.TokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<TokenEntity, String> {
}
