package com.sahin.library_management.repository.jpa;

import com.sahin.library_management.infra.entity.jpa.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
}
