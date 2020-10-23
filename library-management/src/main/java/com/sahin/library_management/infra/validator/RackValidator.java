package com.sahin.library_management.infra.validator;

import com.sahin.library_management.infra.entity.RackEntity;
import com.sahin.library_management.infra.model.book.Rack;
import com.sahin.library_management.repository.RackRepository;
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
        return Rack.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Rack rack = (Rack) o;

        Optional<RackEntity> queryResult = rackRepository.findByLocation(rack.getLocation());

        if (queryResult.isPresent()) {
            errors.rejectValue("location", "ALREADY EXIST", "The rack with same location already exists!");
        }
    }
}
