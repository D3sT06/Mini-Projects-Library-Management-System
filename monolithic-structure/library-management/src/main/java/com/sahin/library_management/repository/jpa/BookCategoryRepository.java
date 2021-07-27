package com.sahin.library_management.repository.jpa;

import com.sahin.library_management.infra.entity.jpa.BookCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookCategoryRepository extends JpaRepository<BookCategoryEntity, Long> {
}
