package com.sahin.lms.library_service.repository;

import com.sahin.lms.infra_entity.library.jpa.BookItemEntity;
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
