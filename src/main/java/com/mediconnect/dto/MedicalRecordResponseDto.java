package com.mediconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordResponseDto {

    private Long id;
    private Long patientId;
    private Long doctorId;
    private Long appointmentId;
    private String diagnosis;
    private String symptoms;
    private String treatment;
    private String doctorNotes;
    private String labReport;
    private LocalDateTime createdAt;
    private List<PrescriptionResponseDto> prescriptions;

    public static MedicalRecordResponseDto fromEntity(com.mediconnect.entity.MedicalRecord record) {
        List<PrescriptionResponseDto> prescriptionDtos = null;
        if (record.getPrescriptions() != null) {
            prescriptionDtos = record.getPrescriptions().stream()
                    .map(PrescriptionResponseDto::fromEntity)
                    .toList();
        }

        return MedicalRecordResponseDto.builder()
                .id(record.getId())
                .patientId(record.getPatient().getId())
                .doctorId(record.getDoctor().getId())
                .appointmentId(record.getAppointment() != null ? record.getAppointment().getId() : null)
                .diagnosis(record.getDiagnosis())
                .symptoms(record.getSymptoms())
                .treatment(record.getTreatmentPlan())
                .doctorNotes(record.getNotes())
                .labReport(record.getLabResults())
                .createdAt(record.getCreatedAt())
                .prescriptions(prescriptionDtos)
                .build();
    }
}
