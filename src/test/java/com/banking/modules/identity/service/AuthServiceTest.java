package com.banking.modules.identity.service;

import com.banking.modules.identity.dto.RegisterRequest;
import com.banking.modules.identity.entity.User;
import com.banking.modules.identity.repository.RoleRepository;
import com.banking.modules.identity.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    // Mock object, completely isolated from DB
    // Can be forced to return specific value by using when method
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest request;

    @BeforeEach
    void setUp() {
        this.request = RegisterRequest.builder()
                .phoneNumber("0906413500")
                .email("valobimat2@email.com")
                .password("StrongPass123!@")
                .identityNumber("201851550")
                .fullName("Nguyen Thanh A")
                .dateOfBirth(LocalDate.of(2002, 7, 18))
                .build();
    }

    @Test
    @DisplayName("Should throw exception when phone number already exists")
    void testRegister_Invalid_PhoneExists() {
        // Arrange
        Mockito.when(this.userRepository.existsByPhoneNumber("0906413500")).thenReturn(true);

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> this.authService.registerUser(this.request));

        // Assert
        assertEquals("Phone number already in use", ex.getMessage());
        Mockito.verify(this.userRepository, Mockito.never()).save(Mockito.any(User.class));
    }

}
