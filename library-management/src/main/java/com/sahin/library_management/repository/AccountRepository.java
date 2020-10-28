package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity.AccountEntity;
import com.sahin.library_management.infra.enums.AccountFor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    @EntityGraph(attributePaths = {"libraryCard"})
    Optional<AccountEntity> findByLibraryCardBarcode(String barcode);

    void deleteByLibraryCardBarcode(String barcode);

    // for preventing n+1 problem
    @EntityGraph(attributePaths = {"libraryCard"})
    List<AccountEntity> findAll();

    @Query("select a from AccountEntity a join fetch a.libraryCard where a.libraryCard.accountFor = :accountFor")
    List<AccountEntity> getAll(@Param("accountFor") AccountFor accountFor);
}
