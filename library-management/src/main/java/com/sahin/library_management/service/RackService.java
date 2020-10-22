package com.sahin.library_management.service;

import com.sahin.library_management.infra.annotation.LogExecutionTime;
import com.sahin.library_management.infra.entity_model.AuthorEntity;
import com.sahin.library_management.infra.entity_model.RackEntity;
import com.sahin.library_management.infra.exception.MyRuntimeException;
import com.sahin.library_management.infra.model.book.Author;
import com.sahin.library_management.infra.model.book.Rack;
import com.sahin.library_management.mapper.RackMapper;
import com.sahin.library_management.repository.RackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@LogExecutionTime
public class RackService {
    @Autowired
    private RackRepository rackRepository;

    @Autowired
    private RackMapper rackMapper;

    @Transactional
    public Rack createRack(Rack rack) {
        if (rack.getId() != null)
            throw new MyRuntimeException("NOT CREATED", "Rack to be created cannot have an id.", HttpStatus.BAD_REQUEST);

        RackEntity entity = rackMapper.toEntity(rack);
        entity = rackRepository.save(entity);
        return rackMapper.toModel(entity);
    }

    @Transactional
    public void updateRack(Rack rack) {
        if (rack.getId() == null)
            throw new MyRuntimeException("NOT UPDATED", "Rack to be updated must have an id.", HttpStatus.BAD_REQUEST);

        if (!rackRepository.findById(rack.getId()).isPresent())
            throw setExceptionWhenRackNotExist(rack.getId());

        RackEntity entity = rackMapper.toEntity(rack);
        rackRepository.save(entity);
    }

    @Transactional
    public void deleteRackById(Long rackId) {
        Optional<RackEntity> optionalEntity = rackRepository.findById(rackId);

        if (!optionalEntity.isPresent())
            throw setExceptionWhenRackNotExist(rackId);

        rackRepository.deleteById(rackId);
    }

    @Transactional
    public Rack getRackById(Long rackId) {
        RackEntity entity = rackRepository
                .findById(rackId)
                .orElseThrow(()-> setExceptionWhenRackNotExist(rackId));

        return rackMapper.toModel(entity);
    }

    @Transactional
    public List<Rack> getAll() {
        List<RackEntity> entities = rackRepository
                .findAll();

        return rackMapper.toModels(entities);
    }

    private MyRuntimeException setExceptionWhenRackNotExist(Long rackId) {
        return new MyRuntimeException("NOT FOUND", "Rack with id \"" + rackId + "\" not exist!", HttpStatus.BAD_REQUEST);
    }
}
