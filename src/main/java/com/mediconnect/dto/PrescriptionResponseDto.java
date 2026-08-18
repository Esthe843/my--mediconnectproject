package com.mediconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionResponseDto {

    private Long id;
    private String medicineName;
    private String dosage;
    private String frequency;
    private int duration;
    private String instructions;

    public static PrescriptionResponseDto fromEntity(com.mediconnect.entity.Prescription p) {
        return PrescriptionResponseDto.builder()
                .id(p.getId())
                .medicineName(p.getMedicationName())
                .dosage(p.getDosage())
                .frequency(p.getFrequency())
                .duration(p.getDurationDays())
                .instructions(p.getInstructions())
                .build();
    }
}
