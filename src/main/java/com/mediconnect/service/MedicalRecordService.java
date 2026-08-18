package com.mediconnect.service;

import com.mediconnect.dto.MedicalRecordRequestDto;
import com.mediconnect.dto.MedicalRecordResponseDto;

import java.util.List;

public interface MedicalRecordService {

    MedicalRecordResponseDto createMedicalRecord(MedicalRecordRequestDto request);

    MedicalRecordResponseDto getMedicalRecordById(Long id);

    List<MedicalRecordResponseDto> getByPatientId(Long patientId);

    List<MedicalRecordResponseDto> getByDoctorId(Long doctorId);

    MedicalRecordResponseDto updateMedicalRecord(Long id, MedicalRecordRequestDto request);

    void deleteMedicalRecord(Long id);
}
