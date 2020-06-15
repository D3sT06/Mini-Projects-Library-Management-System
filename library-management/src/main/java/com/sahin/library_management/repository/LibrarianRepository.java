package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity_model.LibrarianEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibrarianRepository extends JpaRepository<LibrarianEntity, Long> {
    Optional<LibrarianEntity> findByLibraryCardBarcode(String barcode);
    Optional<LibrarianEntity> deleteByLibraryCardBarcode(String barcode);
}
