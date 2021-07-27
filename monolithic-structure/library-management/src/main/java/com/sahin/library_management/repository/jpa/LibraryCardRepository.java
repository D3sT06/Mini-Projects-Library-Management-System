package com.sahin.library_management.repository.jpa;

import com.sahin.library_management.infra.entity.jpa.LibraryCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryCardRepository extends JpaRepository<LibraryCardEntity, String> {
}
