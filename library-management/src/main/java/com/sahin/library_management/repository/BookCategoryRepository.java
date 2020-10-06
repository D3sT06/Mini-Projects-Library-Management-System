package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity_model.BookCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookCategoryRepository extends JpaRepository<BookCategoryEntity, Long> {
}
