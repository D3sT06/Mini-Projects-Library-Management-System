package com.sahin.library_management.repository.jpa;

import com.sahin.library_management.infra.entity.jpa.AccountLoginTypeEntity;
import com.sahin.library_management.infra.enums.LoginType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountLoginTypeRepository extends JpaRepository<AccountLoginTypeEntity, Long> {

    boolean existsByLibraryCardBarcodeAndType(String barcode, LoginType type);

    @EntityGraph(attributePaths = {"libraryCard", "libraryCard.loginTypes"})
    Optional<AccountLoginTypeEntity> findByTypeSpecificKeyAndType(String typeSpecificKey, LoginType type);
}
