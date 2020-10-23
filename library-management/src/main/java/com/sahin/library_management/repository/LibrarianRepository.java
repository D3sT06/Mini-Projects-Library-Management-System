package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity.LibrarianEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibrarianRepository extends JpaRepository<LibrarianEntity, Long> {

    Optional<LibrarianEntity> findByLibraryCardBarcode(String barcode);
    void deleteByLibraryCardBarcode(String barcode);

    // for preventing n+1 problem
    @EntityGraph(attributePaths = {"libraryCard"})
    List<LibrarianEntity> findAll();
}
