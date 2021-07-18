package com.sahin.lms.loan_service.repository;

import com.sahin.lms.infra_entity.loan.jpa.BookItemStateEntity;
import com.sahin.lms.infra_entity.loan.jpa.BookReservingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface BookReservingRepository extends JpaRepository<BookReservingEntity, Long> {
    Optional<BookReservingEntity> findTopByBookItemStateOrderByReservedAtDesc(BookItemStateEntity bookItemStateEntity);
}
