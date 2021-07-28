package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.RackEntity;
import com.sahin.library_management.repository.LibraryRepository;
import com.sahin.library_management.repository.jpa.jpa.RackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RackService {

    @Autowired
    private LibraryRepository libraryRepository;

    public RackEntity createRack(RackEntity rack) {
        return rackRepository.save(rack);
    }

    public RackEntity updateRack(RackEntity rack) {
        return rackRepository.save(rack);
    }

    public void deleteRackById(Long rackId) {
        rackRepository.deleteById(rackId);
    }

    public RackEntity getRackById(Long rackId) {
        return rackRepository
                .findById(rackId)
                .get();
    }

    public List<RackEntity> getAll() {
        return rackRepository.findAll();
    }
}
