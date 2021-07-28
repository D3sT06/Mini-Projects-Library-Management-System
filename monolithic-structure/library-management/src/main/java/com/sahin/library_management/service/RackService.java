package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.jpa.RackEntity;
import com.sahin.library_management.repository.jpa.RackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RackService {

    @Autowired
    private RackRepository rackRepository;

    @Transactional
    public RackEntity createRack(RackEntity rack) {
        return rackRepository.save(rack);
    }

    @Transactional
    public RackEntity updateRack(RackEntity rack) {
        return rackRepository.save(rack);
    }

    @Transactional
    public void deleteRackById(Long rackId) {
        rackRepository.deleteById(rackId);
    }

    @Transactional
    public RackEntity getRackById(Long rackId) {
        return rackRepository
                .findById(rackId)
                .get();
    }

    @Transactional
    public List<RackEntity> getAll() {
        return rackRepository.findAll();
    }
}
