package com.sahin.library_management.bootstrap;

import com.sahin.library_management.infra.entity.jpa.RackEntity;
import com.sahin.library_management.repository.jpa.RackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RackLoader implements Loader<RackEntity> {

    @Autowired
    private RackRepository rackRepository;

    @Override
    public void loadDb() {
        RackEntity rack1 = new RackEntity();
        rack1.setLocation("A-1");
        rackRepository.save(rack1);
    }

    @Override
    public void clearDb() {
        rackRepository.deleteAll();
    }

    @Override
    public List<RackEntity> getAll() {
        return rackRepository.findAll();
    }
}
