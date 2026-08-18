package com.mediconnect.service;

import com.mediconnect.dto.DoctorRequestDto;
import com.mediconnect.dto.DoctorResponseDto;

import java.util.List;

public interface DoctorService {

    DoctorResponseDto createDoctor(Long userId, DoctorRequestDto request);

    List<DoctorResponseDto> getAllDoctors();

    DoctorResponseDto getDoctorById(Long id);

    DoctorResponseDto updateDoctor(Long id, Long userId, DoctorRequestDto request);

    void deleteDoctor(Long id, Long userId);

    DoctorResponseDto getDoctorByUserId(Long userId);

    List<DoctorResponseDto> searchDoctors(String keyword);

    List<DoctorResponseDto> getDoctorsBySpecialization(String specialization);

    List<DoctorResponseDto> getAvailableDoctors();
}
