package com.sahin.lms.loan_service.repository;

import com.sahin.lms.infra_entity.loan.jpa.BookItemStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookItemStateRepository extends JpaRepository<BookItemStateEntity, String> {
}
