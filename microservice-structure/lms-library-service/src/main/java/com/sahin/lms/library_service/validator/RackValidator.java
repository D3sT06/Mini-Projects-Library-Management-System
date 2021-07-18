package com.sahin.lms.library_service.validator;

import com.sahin.lms.infra_entity.library.jpa.RackEntity;
import com.sahin.lms.infra_model.library.model.Rack;
import com.sahin.lms.library_service.repository.RackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class RackValidator implements Validator {

    @Autowired
    private RackRepository rackRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return Rack.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Rack rack = (Rack) o;

        Optional<RackEntity> queryResult;
        if (rack.getId() == null)
            queryResult = rackRepository.findByLocation(rack.getLocation());
        else
            queryResult = rackRepository.findByLocationAndIdIsNot(rack.getLocation(), rack.getId());

        if (queryResult.isPresent()) {
            errors.rejectValue("location", "ALREADY EXIST", "The rack with same location already exists!");
        }
    }
}
