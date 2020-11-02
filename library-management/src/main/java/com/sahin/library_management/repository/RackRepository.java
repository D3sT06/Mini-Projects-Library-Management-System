package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity.RackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RackRepository extends JpaRepository<RackEntity, Long> {

    Optional<RackEntity> findByLocation(String location);
    Optional<RackEntity> findByLocationAndIdIsNot(String location, Long id);
}
