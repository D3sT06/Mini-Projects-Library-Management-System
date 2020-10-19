package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity_model.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {

    Optional<AuthorEntity> findByNameAndSurname(String name, String surname);

}
