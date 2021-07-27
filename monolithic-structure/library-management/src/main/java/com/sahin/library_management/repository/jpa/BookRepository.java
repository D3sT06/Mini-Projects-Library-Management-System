package com.sahin.library_management.repository.jpa;

import com.sahin.library_management.infra.entity.jpa.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
}