package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity_model.BookEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long>, JpaSpecificationExecutor<BookEntity> {

    @EntityGraph(attributePaths = {"author", "category"})
    Optional<BookEntity> findByTitleAndAuthorIdAndCategoryId(String title, Long authorId, Long categoryId);

}