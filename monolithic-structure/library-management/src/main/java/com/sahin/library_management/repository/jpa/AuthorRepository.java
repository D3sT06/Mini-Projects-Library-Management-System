package com.sahin.library_management.repository.jpa;

import com.sahin.library_management.infra.entity.jpa.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {

    Optional<AuthorEntity> findByNameAndSurname(String name, String surname);
    Optional<AuthorEntity> findByNameAndSurnameAndIdIsNot(String name, String surname, Long id);
}
