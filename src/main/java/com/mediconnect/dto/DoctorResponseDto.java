package com.mediconnect.dto;

import com.mediconnect.entity.Doctor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorResponseDto {

    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private Doctor.Specialization specialization;
    private String qualification;
    private int experienceYears;
    private String licenseNumber;
    private String phone;
    private String email;
    private String hospitalName;
    private double consultationFee;
    private boolean available;
    private String about;
    private LocalDateTime createdAt;

    public static DoctorResponseDto fromEntity(Doctor doctor) {
        return DoctorResponseDto.builder()
                .id(doctor.getId())
                .userId(doctor.getUser().getId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .specialization(doctor.getSpecialization())
                .qualification(doctor.getQualification())
                .experienceYears(doctor.getExperienceYears())
                .licenseNumber(doctor.getLicenseNumber())
                .phone(doctor.getPhone())
                .email(doctor.getEmail())
                .hospitalName(doctor.getHospitalName())
                .consultationFee(doctor.getConsultationFee())
                .available(doctor.isAvailable())
                .about(doctor.getAbout())
                .createdAt(doctor.getCreatedAt())
                .build();
    }
}
