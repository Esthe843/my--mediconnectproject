package com.mediconnect.controller;

import com.mediconnect.dto.PatientRequestDto;
import com.mediconnect.dto.PatientResponseDto;
import com.mediconnect.security.JwtService;
import com.mediconnect.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "Patients", description = "Patient profile management endpoints")
public class PatientController {

    private final PatientService patientService;
    private final JwtService jwtService;

    @PostMapping
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    @Operation(summary = "Create patient profile")
    public ResponseEntity<PatientResponseDto> createPatient(
            @Valid @RequestBody PatientRequestDto request,
            HttpServletRequest httpRequest) {

        Long userId = extractUserId(httpRequest);
        PatientResponseDto response = patientService.createPatient(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")
    @Operation(summary = "Get patient by ID")
    public ResponseEntity<PatientResponseDto> getPatient(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    @Operation(summary = "Update patient profile")
    public ResponseEntity<PatientResponseDto> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientRequestDto request,
            HttpServletRequest httpRequest) {

        Long userId = extractUserId(httpRequest);
        return ResponseEntity.ok(patientService.updatePatient(id, userId, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete patient profile")
    public ResponseEntity<Void> deletePatient(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {

        Long userId = extractUserId(httpRequest);
        patientService.deletePatient(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    @Operation(summary = "Get patient by user ID")
    public ResponseEntity<PatientResponseDto> getPatientByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(patientService.getPatientByUserId(userId));
    }

    private Long extractUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtService.extractUserId(token);
        }
        return null;
    }
}
