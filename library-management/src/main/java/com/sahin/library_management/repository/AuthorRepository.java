package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity_model.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
}
