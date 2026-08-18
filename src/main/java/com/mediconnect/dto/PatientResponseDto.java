package com.mediconnect.dto;

import com.mediconnect.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponseDto {

    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Patient.Gender gender;
    private String phone;
    private String email;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String bloodGroup;
    private String allergies;
    private String medicalHistory;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private LocalDateTime createdAt;

    public static PatientResponseDto fromEntity(Patient patient) {
        return PatientResponseDto.builder()
                .id(patient.getId())
                .userId(patient.getUser().getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .phone(patient.getPhone())
                .email(patient.getEmail())
                .address(patient.getAddress())
                .city(patient.getCity())
                .state(patient.getState())
                .postalCode(patient.getPostalCode())
                .bloodGroup(patient.getBloodGroup())
                .allergies(patient.getAllergies())
                .medicalHistory(patient.getMedicalHistory())
                .emergencyContactName(patient.getEmergencyContactName())
                .emergencyContactPhone(patient.getEmergencyContactPhone())
                .createdAt(patient.getCreatedAt())
                .build();
    }
}
