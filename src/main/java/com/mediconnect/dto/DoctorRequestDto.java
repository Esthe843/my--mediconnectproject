package com.mediconnect.dto;

import com.mediconnect.entity.Doctor;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRequestDto {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Specialization is required")
    private Doctor.Specialization specialization;

    @NotBlank(message = "Qualification is required")
    private String qualification;

    @NotNull(message = "Experience years is required")
    @PositiveOrZero(message = "Experience years must be zero or positive")
    private Integer experienceYears;

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    private String hospitalName;

    @NotNull(message = "Consultation fee is required")
    @Positive(message = "Consultation fee must be positive")
    private Double consultationFee;

    private String about;
}
