package com.sahin.library_management.repository.jpa;

import com.sahin.library_management.infra.entity.jpa.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends PagingAndSortingRepository<BookEntity, Long>, JpaSpecificationExecutor<BookEntity> {

    @EntityGraph(attributePaths = {"author", "categories"})
    Page<BookEntity> findAll(Specification<BookEntity> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "categories"})
    Optional<BookEntity> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {"author", "categories"})
    Iterable<BookEntity> findAll();
}