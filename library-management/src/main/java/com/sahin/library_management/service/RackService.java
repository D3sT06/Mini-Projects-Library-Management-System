package com.sahin.library_management.service;

import com.sahin.library_management.infra.annotation.LogExecutionTime;
import com.sahin.library_management.infra.entity.RackEntity;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import com.sahin.library_management.infra.model.book.Book;
import com.sahin.library_management.infra.model.book.Rack;
import com.sahin.library_management.mapper.CyclePreventiveContext;
import com.sahin.library_management.mapper.RackMapper;
import com.sahin.library_management.repository.RackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@LogExecutionTime
@CacheConfig(cacheNames = "racks")
public class RackService {
    @Autowired
    private RackRepository rackRepository;

    @Autowired
    private RackMapper rackMapper;

    @Resource
    private RackService self;

    @Transactional
    @CachePut(key = "#result.id")
    public Rack createRack(Rack rack) {
        if (rack.getId() != null)
            throw new MyRuntimeException("NOT CREATED", "Rack to be created cannot have an id.", HttpStatus.BAD_REQUEST);

        RackEntity entity = rackMapper.toEntity(rack, new CyclePreventiveContext());
        entity = rackRepository.save(entity);
        return rackMapper.toModel(entity, new CyclePreventiveContext());
    }

    @Transactional
    @CachePut(key = "#rack.id")
    public Rack updateRack(Rack rack) {
        if (rack.getId() == null)
            throw new MyRuntimeException("NOT UPDATED", "Rack to be updated must have an id.", HttpStatus.BAD_REQUEST);

        if (!rackRepository.findById(rack.getId()).isPresent())
            throw setExceptionWhenRackNotExist(rack.getId());

        RackEntity entity = rackMapper.toEntity(rack, new CyclePreventiveContext());
        entity = rackRepository.save(entity);
        return rackMapper.toModel(entity, new CyclePreventiveContext());
    }

    @Transactional
    @CacheEvict(key = "#rackId")
    public void deleteRackById(Long rackId) {
        Optional<RackEntity> optionalEntity = rackRepository.findById(rackId);

        if (!optionalEntity.isPresent())
            throw setExceptionWhenRackNotExist(rackId);

        rackRepository.deleteById(rackId);
    }

    @Transactional
    @Cacheable(key = "#rackId")
    public Rack getRackById(Long rackId) {
        RackEntity entity = rackRepository
                .findById(rackId)
                .orElseThrow(()-> setExceptionWhenRackNotExist(rackId));

        return rackMapper.toModel(entity, new CyclePreventiveContext());
    }

    @Transactional
    public List<Rack> getAll() {
        List<RackEntity> entities = rackRepository.findAll();
        List<Rack> models = rackMapper.toModels(entities, new CyclePreventiveContext());

        for (Rack model : models)
            self.cache(model);

        return models;
    }

    @CachePut(key = "#rack.id")
    public Rack cache(Rack rack) {
        return rack;
    }

    private MyRuntimeException setExceptionWhenRackNotExist(Long rackId) {
        return new MyRuntimeException("NOT FOUND", "Rack with id \"" + rackId + "\" not exist!", HttpStatus.BAD_REQUEST);
    }
}
