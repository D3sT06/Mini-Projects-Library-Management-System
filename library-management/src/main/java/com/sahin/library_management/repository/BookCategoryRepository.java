package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity_model.BookCategoryEntity;
import com.sahin.library_management.infra.model.book.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookCategoryRepository extends JpaRepository<BookCategoryEntity, Long> {

    Optional<BookCategoryEntity> findByName(String name);
}
