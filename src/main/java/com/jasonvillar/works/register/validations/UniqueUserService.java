package com.jasonvillar.works.register.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueUserServiceValidator.class)
public @interface UniqueUserService {
    String message() default "the combination of userId and serviceId must be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
