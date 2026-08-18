package com.mediconnect.controller;

import com.mediconnect.dto.LoginRequestDto;
import com.mediconnect.dto.RegisterRequestDto;
import com.mediconnect.dto.UserResponseDto;
import com.mediconnect.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User registration, login, and profile endpoints")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account. Passwords must match and be at least 8 characters.")
    public ResponseEntity<UserResponseDto> register(
            @Valid @RequestBody RegisterRequestDto request) {
        UserResponseDto response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticates a user and returns their profile.")
    public ResponseEntity<UserResponseDto> login(
            @Valid @RequestBody LoginRequestDto request) {
        UserResponseDto response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile/{userId}")
    @Operation(summary = "Get user profile", description = "Returns the user profile by user ID.")
    public ResponseEntity<UserResponseDto> getProfile(@PathVariable Long userId) {
        UserResponseDto response = userService.getProfile(userId);
        return ResponseEntity.ok(response);
    }
}
