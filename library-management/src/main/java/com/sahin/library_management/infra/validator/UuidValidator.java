package com.sahin.library_management.infra.validator;

import com.sahin.library_management.infra.annotation.NullableUUIDFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

public class UuidValidator implements ConstraintValidator<NullableUUIDFormat, String> {


    @Override
    public void initialize(NullableUUIDFormat constraintAnnotation) {
        return;
    }

    @Override
    public boolean isValid(String uuid, ConstraintValidatorContext context) {
        if (uuid == null)
            return true;

        return uuid.equals(new UUID(0,0).toString());
    }
}
