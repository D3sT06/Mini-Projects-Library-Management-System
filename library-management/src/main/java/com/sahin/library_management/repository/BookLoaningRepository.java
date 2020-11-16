package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity.AccountEntity;
import com.sahin.library_management.infra.entity.BookItemEntity;
import com.sahin.library_management.infra.entity.BookLoaningEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookLoaningRepository  extends JpaRepository<BookLoaningEntity, Long> {
    Optional<BookLoaningEntity> findTopByBookItemBarcodeOrderByLoanedAtDesc(String bookItemBarcode);
    int countByMemberIdAndReturnedAtIsNull(Long memberId);
}