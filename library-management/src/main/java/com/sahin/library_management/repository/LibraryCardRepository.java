package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity.LibraryCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryCardRepository extends JpaRepository<LibraryCardEntity, String> {
}
