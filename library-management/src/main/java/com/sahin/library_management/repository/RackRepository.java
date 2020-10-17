package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity_model.RackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RackRepository extends JpaRepository<RackEntity, Long> {

    Optional<RackEntity> findByLocation(String location);
}
