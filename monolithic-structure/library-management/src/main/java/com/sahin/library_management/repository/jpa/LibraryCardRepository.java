package com.sahin.library_management.repository.jpa;

import com.sahin.library_management.infra.entity.jpa.LibraryCardEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibraryCardRepository extends JpaRepository<LibraryCardEntity, String> {

    @EntityGraph(attributePaths = {"loginTypes"})
    Optional<LibraryCardEntity> findByBarcode(String barcode);
}
