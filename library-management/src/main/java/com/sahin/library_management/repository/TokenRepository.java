package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity.TokenEntity;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<TokenEntity, String> {
}
