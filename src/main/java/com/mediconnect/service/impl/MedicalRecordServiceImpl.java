package com.mediconnect.service.impl;

import com.mediconnect.dto.MedicalRecordRequestDto;
import com.mediconnect.dto.MedicalRecordResponseDto;
import com.mediconnect.dto.PrescriptionRequestDto;
import com.mediconnect.entity.*;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.*;
import com.mediconnect.service.MedicalRecordService;
import com.mediconnect.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public MedicalRecordResponseDto createMedicalRecord(MedicalRecordRequestDto request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", request.getPatientId()));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", request.getDoctorId()));

        Appointment appointment = null;
        if (request.getAppointmentId() != null) {
            appointment = appointmentRepository.findById(request.getAppointmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", request.getAppointmentId()));
        }

        MedicalRecord record = MedicalRecord.builder()
                .patient(patient)
                .doctor(doctor)
                .appointment(appointment)
                .diagnosis(request.getDiagnosis())
                .symptoms(request.getSymptoms())
                .treatmentPlan(request.getTreatment())
                .notes(request.getDoctorNotes())
                .labResults(request.getLabReport())
                .prescriptions(new ArrayList<>())
                .build();

        // Add prescriptions
        if (request.getPrescriptions() != null) {
            for (PrescriptionRequestDto pReq : request.getPrescriptions()) {
                Prescription prescription = Prescription.builder()
                        .medicalRecord(record)
                        .medicationName(pReq.getMedicineName())
                        .dosage(pReq.getDosage())
                        .frequency(pReq.getFrequency())
                        .durationDays(pReq.getDuration())
                        .instructions(pReq.getInstructions())
                        .build();
                record.getPrescriptions().add(prescription);
            }
        }

        MedicalRecord saved = medicalRecordRepository.save(record);

        // Notify patient about new record
        notificationService.notify(
                patient.getUser().getId(),
                "PRESCRIPTION_ADDED",
                "Medical Record Created",
                "A new medical record has been created for you with diagnosis: " + request.getDiagnosis()
        );

        return MedicalRecordResponseDto.fromEntity(saved);
    }

    @Override
    public MedicalRecordResponseDto getMedicalRecordById(Long id) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical Record", "id", id));
        return MedicalRecordResponseDto.fromEntity(record);
    }

    @Override
    public List<MedicalRecordResponseDto> getByPatientId(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId).stream()
                .map(MedicalRecordResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalRecordResponseDto> getByDoctorId(Long doctorId) {
        return medicalRecordRepository.findByDoctorId(doctorId).stream()
                .map(MedicalRecordResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MedicalRecordResponseDto updateMedicalRecord(Long id, MedicalRecordRequestDto request) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical Record", "id", id));

        record.setDiagnosis(request.getDiagnosis());
        record.setSymptoms(request.getSymptoms());
        record.setTreatmentPlan(request.getTreatment());
        record.setNotes(request.getDoctorNotes());
        record.setLabResults(request.getLabReport());

        MedicalRecord saved = medicalRecordRepository.save(record);
        return MedicalRecordResponseDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public void deleteMedicalRecord(Long id) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical Record", "id", id));
        medicalRecordRepository.delete(record);
    }
}
