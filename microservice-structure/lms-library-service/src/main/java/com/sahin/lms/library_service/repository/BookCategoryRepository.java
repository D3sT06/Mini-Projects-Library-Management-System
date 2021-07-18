package com.sahin.lms.library_service.repository;

import com.sahin.lms.infra_entity.library.jpa.BookCategoryEntity;
import com.sahin.lms.library_service.projection.CategoryProjections;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategoryEntity, Long> {

    Optional<BookCategoryEntity> findByName(String name);
    Optional<BookCategoryEntity> findByNameAndIdIsNot(String name, Long id);

    @EntityGraph(attributePaths = {"books"})
    List<CategoryProjections.CategoryView> findAllProjectedBy();

    @EntityGraph(attributePaths = {"books"})
    Optional<CategoryProjections.CategoryView> findProjectedById(Long id);

}
