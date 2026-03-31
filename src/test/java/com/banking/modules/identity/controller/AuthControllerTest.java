package com.banking.modules.identity.controller;

import com.banking.common.response.ApiResponse;
import com.banking.modules.identity.dto.RegisterRequest;
import com.banking.modules.identity.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    // Mockito creates real authController (by new keyword)
    // After that, auto-inject the mock objects
    @InjectMocks
    private AuthController authController;

    // Mock object, every methods inside it return default value
    @Mock
    private AuthService authService;

    @Test
    @DisplayName("Should return 201 CREATED and call service when request is valid")
    void testRegister_normal() {
        // Arrange
        RegisterRequest request = RegisterRequest.builder()
                .phoneNumber("0906413500")
                .email("valobimat2@email.com")
                .password("StrongPass123!@")
                .identityNumber("201851550")
                .fullName("Nguyen Thanh A")
                .dateOfBirth(LocalDate.of(2002, 7, 18))
                .build();

        // Act
        ResponseEntity<ApiResponse<Void>> response = this.authController.register(request);

        // Assert
        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Account was registered successfully, please verify the OTP",
                response.getBody().getMessage());

        Mockito.verify(authService, Mockito.times(1)).registerUser(request);
    }

}
