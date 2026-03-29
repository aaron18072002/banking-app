package com.banking.modules.identity.service;

import com.banking.modules.identity.dto.RegisterRequest;
import com.banking.modules.identity.entity.Role;
import com.banking.modules.identity.entity.User;
import com.banking.modules.identity.entity.UserStatus;
import com.banking.modules.identity.repository.RoleRepository;
import com.banking.modules.identity.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(RegisterRequest request) {

        log.info("Registering user for: {}", request.phoneNumber());

        // validate input request
        if(this.userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new IllegalArgumentException("Phone number already in use");
        }
        if(this.userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (this.userRepository.existsByIdentityNumber(request.identityNumber())) {
            throw new IllegalArgumentException("Identity number already in use");
        }

        // Default role for register is CUSTOMER ROLE
        Role customerRole = this.roleRepository.findByCode("ROLE_CUSTOMER")
                .orElseThrow(() -> new IllegalStateException("Role not found"));

        User user = new User();
        user.setPhoneNumber(request.phoneNumber());
        user.setEmail(request.email());
        user.setPasswordHash(this.passwordEncoder.encode(request.password()));
        user.setIdentityNumber(request.identityNumber());
        user.setFullName(request.fullName());
        user.setDateOfBirth(request.dateOfBirth());

        // Waiting for verify eKYC/OTP
        user.setUserStatus(UserStatus.PENDING_VERIFICATION);
        user.setRoles(Set.of(customerRole));

        this.userRepository.save(user);

        log.info("Registered user for: {}, waiting for verify OTP", request.phoneNumber());

    }

}
