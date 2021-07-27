package com.sahin.library_management.repository.jpa;

import com.sahin.library_management.infra.entity.jpa.BookCategoryEntity;
import com.sahin.library_management.infra.projections.CategoryProjections;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookCategoryRepository extends JpaRepository<BookCategoryEntity, Long> {

    List<CategoryProjections.CategoryView> findAllProjectedBy();

    Optional<CategoryProjections.CategoryView> findProjectedById(Long id);

}
