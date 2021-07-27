package com.sahin.library_management.repository.jpa;

import com.sahin.library_management.infra.entity.jpa.AuthorEntity;
import com.sahin.library_management.infra.projections.AuthorProjections;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {

    List<AuthorProjections.AuthorView> findAllProjectedBy();

    Optional<AuthorProjections.AuthorView> findProjectedById(Long id);
}
