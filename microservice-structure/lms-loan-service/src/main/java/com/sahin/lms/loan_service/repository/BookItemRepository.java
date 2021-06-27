package com.sahin.lms.loan_service.repository;

import com.sahin.lms.infra.entity.jpa.BookItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookItemRepository extends JpaRepository<BookItemEntity, String> {
}
