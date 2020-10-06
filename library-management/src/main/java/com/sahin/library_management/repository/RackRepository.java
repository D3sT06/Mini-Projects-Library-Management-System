package com.sahin.library_management.repository;

import com.sahin.library_management.infra.entity_model.RackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RackRepository extends JpaRepository<RackEntity, Long> {
}
