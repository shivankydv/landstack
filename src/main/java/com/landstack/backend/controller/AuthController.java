package com.landstack.backend.controller;

import com.landstack.backend.dto.LoginRequest;
import com.landstack.backend.dto.LoginResponse;
import com.landstack.backend.entity.User;
import com.landstack.backend.service.CustomUserDetailsService;
import com.landstack.backend.service.JwtService;
import com.landstack.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final CustomUserDetailsService userDetailsService;
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            CustomUserDetailsService userDetailsService,
            UserService userService,
            JwtService jwtService,
            PasswordEncoder passwordEncoder) {

        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        request.getUsername()
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                userDetails.getPassword())) {

            return ResponseEntity.status(401).build();
        }

        User user =
                userService.getUserByUsername(
                        request.getUsername()
                );

        String token = jwtService.generateToken(userDetails);

        String role = userDetails.getAuthorities()
                .iterator()
                .next()
                .getAuthority()
                .replace("ROLE_", "");

        LoginResponse response = new LoginResponse(
                "Login successful",
                user.getId(),
                user.getUsername(),
                role
        );

        response.setToken(token);

        return ResponseEntity.ok(response);
    }
}