package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity.AuthorEntity;
import com.sahin.library_management.infra.projections.AuthorProjections;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {

    Optional<AuthorEntity> findByNameAndSurname(String name, String surname);
    Optional<AuthorEntity> findByNameAndSurnameAndIdIsNot(String name, String surname, Long id);

    @EntityGraph(attributePaths = {"books"})
    List<AuthorProjections.AuthorView> findAllProjectedBy();

    @EntityGraph(attributePaths = {"books"})
    Optional<AuthorProjections.AuthorView> findProjectedById(Long id);
}
