package com.sahin.library_management.repository.jpa;

import com.sahin.library_management.infra.entity.jpa.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findByLibraryCardBarcode(String barcode);

    void deleteByLibraryCardBarcode(String barcode);

    List<AccountEntity> findAll();
}