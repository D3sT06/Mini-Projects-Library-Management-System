package com.sahin.library_management.repository.jpa;

import com.sahin.library_management.infra.entity.jpa.BookItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookItemRepository extends JpaRepository<BookItemEntity, String> {

    List<BookItemEntity> findByBookId(Long bookId);
    Optional<BookItemEntity> findById(String id);
}
