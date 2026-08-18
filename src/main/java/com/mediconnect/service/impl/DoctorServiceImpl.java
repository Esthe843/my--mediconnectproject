package com.mediconnect.service.impl;

import com.mediconnect.dto.DoctorRequestDto;
import com.mediconnect.dto.DoctorResponseDto;
import com.mediconnect.entity.Doctor;
import com.mediconnect.entity.User;
import com.mediconnect.exception.DuplicateResourceException;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.DoctorRepository;
import com.mediconnect.repository.UserRepository;
import com.mediconnect.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public DoctorResponseDto createDoctor(Long userId, DoctorRequestDto request) {
        if (doctorRepository.existsByUserId(userId)) {
            throw new DuplicateResourceException("Doctor profile", "userId", String.valueOf(userId));
        }
        if (doctorRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new DuplicateResourceException("Doctor", "licenseNumber", request.getLicenseNumber());
        }
        if (doctorRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Doctor", "email", request.getEmail());
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Doctor doctor = Doctor.builder()
                .user(user)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .specialization(request.getSpecialization())
                .qualification(request.getQualification())
                .experienceYears(request.getExperienceYears())
                .licenseNumber(request.getLicenseNumber())
                .phone(request.getPhone())
                .email(request.getEmail())
                .hospitalName(request.getHospitalName())
                .consultationFee(request.getConsultationFee())
                .about(request.getAbout())
                .available(true)
                .build();

        return DoctorResponseDto.fromEntity(doctorRepository.save(doctor));
    }

    @Override
    public List<DoctorResponseDto> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(DoctorResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorResponseDto getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
        return DoctorResponseDto.fromEntity(doctor);
    }

    @Override
    @Transactional
    public DoctorResponseDto updateDoctor(Long id, Long userId, DoctorRequestDto request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));

        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setQualification(request.getQualification());
        doctor.setExperienceYears(request.getExperienceYears());
        doctor.setPhone(request.getPhone());
        doctor.setEmail(request.getEmail());
        doctor.setHospitalName(request.getHospitalName());
        doctor.setConsultationFee(request.getConsultationFee());
        doctor.setAbout(request.getAbout());

        return DoctorResponseDto.fromEntity(doctorRepository.save(doctor));
    }

    @Override
    @Transactional
    public void deleteDoctor(Long id, Long userId) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
        doctorRepository.delete(doctor);
    }

    @Override
    public DoctorResponseDto getDoctorByUserId(Long userId) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile", "userId", userId));
        return DoctorResponseDto.fromEntity(doctor);
    }

    @Override
    public List<DoctorResponseDto> searchDoctors(String keyword) {
        return doctorRepository.searchByKeyword(keyword).stream()
                .map(DoctorResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponseDto> getDoctorsBySpecialization(String specialization) {
        Doctor.Specialization spec = Doctor.Specialization.valueOf(specialization.toUpperCase());
        return doctorRepository.findBySpecialization(spec).stream()
                .map(DoctorResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponseDto> getAvailableDoctors() {
        return doctorRepository.findByAvailable(true).stream()
                .map(DoctorResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
