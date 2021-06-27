package com.sahin.library_management.infra.annotation;


import com.sahin.library_management.infra.validator.UuidValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UuidValidator.class)
public @interface NullableUUIDFormat {

    String message() default "Not valid format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
