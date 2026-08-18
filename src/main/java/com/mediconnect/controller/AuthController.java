package com.mediconnect.controller;

import com.mediconnect.dto.AuthResponseDto;
import com.mediconnect.dto.LoginRequestDto;
import com.mediconnect.dto.RegisterRequestDto;
import com.mediconnect.dto.UserResponseDto;
import com.mediconnect.security.JwtService;
import com.mediconnect.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User registration, login, and profile endpoints")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account.")
    public ResponseEntity<UserResponseDto> register(
            @Valid @RequestBody RegisterRequestDto request) {
        UserResponseDto response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticates and returns a JWT token.")
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody LoginRequestDto request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserResponseDto userProfile = userService.login(request);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userProfile.getId());
        claims.put("role", userProfile.getRole().name());
        String jwtToken = jwtService.generateToken(claims, userDetails);

        AuthResponseDto response = AuthResponseDto.builder()
                .token(jwtToken)
                .role(userProfile.getRole().name())
                .userId(userProfile.getId())
                .expiresIn(String.valueOf(jwtService.getExpirationMs()))
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile/{userId}")
    @Operation(summary = "Get user profile", description = "Returns the user profile by user ID.")
    public ResponseEntity<UserResponseDto> getProfile(@PathVariable Long userId) {
        UserResponseDto response = userService.getProfile(userId);
        return ResponseEntity.ok(response);
    }
}
