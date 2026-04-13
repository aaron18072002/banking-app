package com.banking.modules.identity.service;

import com.banking.modules.identity.dto.LoginRequest;
import com.banking.modules.identity.dto.LoginResponse;
import com.banking.modules.identity.dto.RegisterRequest;
import com.banking.modules.identity.entity.Role;
import com.banking.modules.identity.entity.User;
import com.banking.modules.identity.entity.UserStatus;
import com.banking.modules.identity.repository.RoleRepository;
import com.banking.modules.identity.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String SIGNER_KEY;

    /**
     *  Validate credentials and process register account logic
     *
     * @param request RegisterRequest
     */
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

    /**
     * Authenticates a user based on their email and password, bypassing the default
     * Spring Security AuthenticationManager for explicit, manual control over the flow.
     *
     * @param request The login payload containing the user's email and raw password.
     * @return A {@link LoginResponse} containing the signed JWT and its metadata.
     * @throws IllegalArgumentException if the email is not found in the database
     *  or if the provided password does not match.
     * @throws IllegalStateException if the user is found but their status is not ACTIVE.
     */
    public LoginResponse login(LoginRequest request) {

        log.info("Start login for email: {}", request.email());

        User user = this.userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Email is not found"));

        boolean authenticated = this.passwordEncoder.matches(request.password(), user.getPasswordHash());
        if(!authenticated) {
            throw new IllegalArgumentException("Wrong password");
        }

        if(user.getUserStatus() != UserStatus.ACTIVE) {
            throw new IllegalStateException("User is not active");
        }

        String accessToken = this.generateAccessToken(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .expiresIn(3600000L) // 1 HOUR
                .build();
    }

    /**
     * Generates a signed JSON Web Token (JWT) using the HMAC SHA-512 (HS512) algorithm.
     *
     * @param user The authenticated user entity.
     * @return A serialized, cryptographically signed JWT string.
     * @throws RuntimeException if the token signing process fails due to invalid keys or algorithms.
     */
    private String generateAccessToken(User user) {

        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("banking-app")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("userId", user.getId().toString())
                .claim("scope", this.buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error happened when create JWT accessToken, ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Flattens the user's roles and permissions into a single,
     *  space-separated string
     *
     * @param user The user entity containing collections of Roles and Permissions.
     * @return A space-separated string representing all granted authorities.
     */
    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if(!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add(role.getCode());
                if(!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> {
                        stringJoiner.add(permission.getCode());
                    });
                }
            });
        }
        return stringJoiner.toString();
    }

}
