package com.banking.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AgeLimitValidator implements ConstraintValidator<AgeLimit, LocalDate> {

    private int minimumAge;

    @Override
    public void initialize(AgeLimit constraintAnnotation) {
        this.minimumAge = constraintAnnotation.minimumAge();
    }

    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext constraintValidatorContext) {
        // Let the @NotNull annotation handle null checks
        // A custom validator should ONLY focus on its specific business rule.
        if(dateOfBirth == null) {
            return true;
        }

        int age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        return age >= this.minimumAge;
    }

}
