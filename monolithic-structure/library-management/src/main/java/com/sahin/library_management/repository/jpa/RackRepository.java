package com.sahin.library_management.repository.jpa;

import com.sahin.library_management.infra.entity.jpa.RackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RackRepository extends JpaRepository<RackEntity, Long> {
}
