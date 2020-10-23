package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity.BookCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookCategoryRepository extends JpaRepository<BookCategoryEntity, Long> {

    Optional<BookCategoryEntity> findByName(String name);
}
