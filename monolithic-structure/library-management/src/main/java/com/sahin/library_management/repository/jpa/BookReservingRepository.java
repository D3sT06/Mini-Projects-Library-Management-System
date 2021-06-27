package com.sahin.library_management.repository.jpa;

import com.sahin.library_management.infra.entity.jpa.BookItemEntity;
import com.sahin.library_management.infra.entity.jpa.BookReservingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface BookReservingRepository extends JpaRepository<BookReservingEntity, Long> {

    Optional<BookReservingEntity> findTopByBookItemOrderByReservedAtDesc(BookItemEntity bookItemEntity);
}
