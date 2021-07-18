package com.sahin.lms.loan_service.repository;

import com.sahin.lms.infra_entity.loan.jpa.BookLoaningEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookLoaningRepository  extends JpaRepository<BookLoaningEntity, Long> {
    Optional<BookLoaningEntity> findTopByBookItemStateItemBarcodeOrderByLoanedAtDesc(String bookItemBarcode);
    int countByMemberIdAndReturnedAtIsNull(Long memberId);
}