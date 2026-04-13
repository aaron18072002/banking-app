package com.banking.modules.identity.dto;

import lombok.Builder;

@Builder
public record LoginResponse(

    String accessToken,
    String tokenType,
    long expiresIn

) {}
