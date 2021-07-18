package com.sahin.lms.library_service.repository;

import com.sahin.lms.infra_entity.library.jpa.RackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RackRepository extends JpaRepository<RackEntity, Long> {

    Optional<RackEntity> findByLocation(String location);
    Optional<RackEntity> findByLocationAndIdIsNot(String location, Long id);
}