package com.banking.modules.identity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginRequest(

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email is incorrect format")
    String email,

    @NotBlank(message = "Password cannot be empty")
    String password

) {}
