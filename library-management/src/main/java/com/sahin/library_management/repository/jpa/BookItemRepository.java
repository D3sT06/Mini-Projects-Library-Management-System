package com.sahin.library_management.repository.jpa;

import com.sahin.library_management.infra.entity.jpa.BookItemEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookItemRepository extends JpaRepository<BookItemEntity, String> {

    @EntityGraph(attributePaths = {"book", "rack", "book.author", "book.categories"})
    List<BookItemEntity> findByBookId(Long bookId);

    @EntityGraph(attributePaths = {"book", "rack", "book.author", "book.categories"})
    Optional<BookItemEntity> findById(String id);
}
