package com.banking.modules.identity.controller;

import com.banking.common.response.ApiResponse;
import com.banking.modules.identity.dto.RegisterRequest;
import com.banking.modules.identity.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
            @Valid @RequestBody RegisterRequest registerRequest) {

        this.authService.registerUser(registerRequest);

        ApiResponse<Void> response = ApiResponse.success(
                HttpStatus.CREATED.value(),
                "Account was registered successfully, please verify the OTP",
                null
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

}
