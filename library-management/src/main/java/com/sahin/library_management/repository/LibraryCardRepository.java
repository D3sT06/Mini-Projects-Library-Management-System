package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity.LibraryCardEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibraryCardRepository extends JpaRepository<LibraryCardEntity, String> {

    @EntityGraph(attributePaths = {"loginTypes"})
    Optional<LibraryCardEntity> findByBarcode(String barcode);
}
