package com.banking.modules.identity.dto;

import com.banking.common.validation.AgeLimit;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record RegisterRequest(

       @NotBlank(message = "Phone number cannot be blank")
       @Pattern(regexp = "^(0|\\+84)[35789][0-9]{8}$", message = "Invalid phone number format")
       String phoneNumber,

       @NotBlank(message = "Email cannot be blank")
       @Email(message = "Invalid email format")
       @Size(max = 50, message = "Email cannot exceed 50 characters")
       String email,

       @NotBlank(message = "Password cannot be blank")
       @Pattern(
               regexp = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!_]).{8,}$",
       message = "Password must be at least 8 characters long, contain at least one uppercase letter, one number, and one special character")
       String password,

       @NotBlank(message = "Identity number cannot be blank")
       @Pattern(regexp = "^[0-9]{12}$", message = "Identity number must contain exactly 12 digits")
       String identityNumber,

       @NotBlank(message = "Full name cannot be blank")
       @Size(max = 50, message = "Full name cannot exceed 50 characters")
       String fullName,

       @NotNull(message = "Date of birth cannot be null")
       @AgeLimit(minimumAge = 18, message = "You must be at least 18 years old to open a bank account")
       @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
       LocalDate dateOfBirth

) {};
