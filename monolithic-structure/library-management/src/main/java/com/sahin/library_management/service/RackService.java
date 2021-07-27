package com.sahin.library_management.service;

import com.sahin.library_management.infra.entity.jpa.RackEntity;
import com.sahin.library_management.infra.model.book.Rack;
import com.sahin.library_management.mapper.CyclePreventiveContext;
import com.sahin.library_management.mapper.RackMapper;
import com.sahin.library_management.repository.jpa.RackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RackService {

    @Autowired
    private RackRepository rackRepository;

    @Autowired
    private RackMapper rackMapper;

    @Transactional
    public Rack createRack(Rack rack) {
        RackEntity entity = rackMapper.toEntity(rack, new CyclePreventiveContext());
        entity = rackRepository.save(entity);
        return rackMapper.toModel(entity, new CyclePreventiveContext());
    }

    @Transactional
    public Rack updateRack(Rack rack) {
        RackEntity entity = rackMapper.toEntity(rack, new CyclePreventiveContext());
        entity = rackRepository.save(entity);
        return rackMapper.toModel(entity, new CyclePreventiveContext());
    }

    @Transactional
    public void deleteRackById(Long rackId) {
        rackRepository.deleteById(rackId);
    }

    @Transactional
    public Rack getRackById(Long rackId) {
        RackEntity entity = rackRepository
                .findById(rackId)
                .get();

        return rackMapper.toModel(entity, new CyclePreventiveContext());
    }

    @Transactional
    public List<Rack> getAll() {
        List<RackEntity> entities = rackRepository.findAll();
        return rackMapper.toModels(entities, new CyclePreventiveContext());
    }
}
