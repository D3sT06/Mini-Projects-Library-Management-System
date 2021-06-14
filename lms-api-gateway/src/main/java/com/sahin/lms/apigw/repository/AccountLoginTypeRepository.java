package com.sahin.lms.apigw.repository;

import com.sahin.lms.infra.auth.entity.AccountLoginTypeEntity;
import com.sahin.lms.infra.auth.enums.LoginType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountLoginTypeRepository extends JpaRepository<AccountLoginTypeEntity, Long> {

    boolean existsByLibraryCardBarcodeAndType(String barcode, LoginType type);

    @EntityGraph(attributePaths = {"libraryCard", "libraryCard.loginTypes"})
    Optional<AccountLoginTypeEntity> findByTypeSpecificKeyAndType(String typeSpecificKey, LoginType type);
}
