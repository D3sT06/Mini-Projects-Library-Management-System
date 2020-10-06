package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity_model.LibrarianEntity;
import com.sahin.library_management.infra.entity_model.MemberEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByLibraryCardBarcode(String barcode);
    void deleteByLibraryCardBarcode(String barcode);

    // for preventing n+1 problem
    @EntityGraph(attributePaths = {"libraryCard"})
    List<MemberEntity> findAll();
}
