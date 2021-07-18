package com.sahin.lms.library_service.repository;

import com.sahin.lms.infra_entity.library.jpa.AuthorEntity;
import com.sahin.lms.library_service.projection.AuthorProjections;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {

    Optional<AuthorEntity> findByNameAndSurname(String name, String surname);
    Optional<AuthorEntity> findByNameAndSurnameAndIdIsNot(String name, String surname, Long id);

    @EntityGraph(attributePaths = {"books"})
    List<AuthorProjections.AuthorView> findAllProjectedBy();

    @EntityGraph(attributePaths = {"books"})
    Optional<AuthorProjections.AuthorView> findProjectedById(Long id);
}
