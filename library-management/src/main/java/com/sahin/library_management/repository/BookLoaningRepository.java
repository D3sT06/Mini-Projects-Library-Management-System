package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity_model.BookItemEntity;
import com.sahin.library_management.infra.entity_model.BookLoaningEntity;
import com.sahin.library_management.infra.entity_model.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookLoaningRepository  extends JpaRepository<BookLoaningEntity, Long> {
    Optional<BookLoaningEntity> findTopByBookItemOrderByLoanedAtDesc(BookItemEntity bookItemEntity);
    int countByMemberAndReturnedAtIsNull(MemberEntity memberEntity);
}