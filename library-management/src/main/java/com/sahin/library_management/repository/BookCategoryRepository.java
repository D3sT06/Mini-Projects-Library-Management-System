package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity.BookCategoryEntity;
import com.sahin.library_management.infra.projections.CategoryProjections;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookCategoryRepository extends JpaRepository<BookCategoryEntity, Long> {

    Optional<BookCategoryEntity> findByName(String name);
    Optional<BookCategoryEntity> findByNameAndIdIsNot(String name, Long id);

    @EntityGraph(attributePaths = {"books"})
    List<CategoryProjections.CategoryView> findAllProjectedBy();

    @EntityGraph(attributePaths = {"books"})
    Optional<CategoryProjections.CategoryView> findProjectedById(Long id);

}
