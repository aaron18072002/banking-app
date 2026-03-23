package com.banking.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AgeLimitValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AgeLimit {

    String message() default "You must be at least 18 years old";

    int minimumAge() default 18;

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
