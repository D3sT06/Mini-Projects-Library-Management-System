package com.sahin.lms.infra.validator;

import com.sahin.library_management.infra.annotation.NullableUUIDFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

public class UuidValidator implements ConstraintValidator<NullableUUIDFormat, String> {

    @Override
    public boolean isValid(String uuid, ConstraintValidatorContext context) {
        if (uuid == null)
            return true;

        try {
            UUID uuidInstance = UUID.fromString(uuid);
            return  !uuidInstance.equals(new UUID(0,0));
        } catch (Exception e) {
            return false;
        }
    }
}
