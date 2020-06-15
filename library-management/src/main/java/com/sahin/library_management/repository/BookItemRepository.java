package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity_model.BookItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookItemRepository extends JpaRepository<BookItemEntity, String> {
}
