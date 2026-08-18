package com.mediconnect.service;

import com.mediconnect.dto.PatientRequestDto;
import com.mediconnect.dto.PatientResponseDto;

public interface PatientService {

    PatientResponseDto createPatient(Long userId, PatientRequestDto request);

    PatientResponseDto getPatientById(Long id);

    PatientResponseDto updatePatient(Long id, Long userId, PatientRequestDto request);

    void deletePatient(Long id, Long userId);

    PatientResponseDto getPatientByUserId(Long userId);
}
