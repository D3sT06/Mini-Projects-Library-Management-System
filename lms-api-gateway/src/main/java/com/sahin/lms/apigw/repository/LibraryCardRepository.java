package com.sahin.lms.apigw.repository;

import com.sahin.lms.infra.entity.jpa.LibraryCardEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibraryCardRepository extends JpaRepository<LibraryCardEntity, String> {

    @EntityGraph(attributePaths = {"loginTypes"})
    Optional<LibraryCardEntity> findByBarcode(String barcode);
}
