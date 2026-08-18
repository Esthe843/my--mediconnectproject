package com.mediconnect.service.impl;

import com.mediconnect.dto.PatientRequestDto;
import com.mediconnect.dto.PatientResponseDto;
import com.mediconnect.entity.Patient;
import com.mediconnect.entity.User;
import com.mediconnect.exception.DuplicateResourceException;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.PatientRepository;
import com.mediconnect.repository.UserRepository;
import com.mediconnect.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PatientResponseDto createPatient(Long userId, PatientRequestDto request) {
        if (patientRepository.existsByUserId(userId)) {
            throw new DuplicateResourceException("Patient profile", "userId", String.valueOf(userId));
        }

        if (patientRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Patient", "email", request.getEmail());
        }

        if (patientRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateResourceException("Patient", "phone", request.getPhone());
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Patient patient = Patient.builder()
                .user(user)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .postalCode(request.getPostalCode())
                .bloodGroup(request.getBloodGroup())
                .allergies(request.getAllergies())
                .medicalHistory(request.getMedicalHistory())
                .emergencyContactName(request.getEmergencyContactName())
                .emergencyContactPhone(request.getEmergencyContactPhone())
                .build();

        Patient saved = patientRepository.save(patient);
        return PatientResponseDto.fromEntity(saved);
    }

    @Override
    public PatientResponseDto getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));
        return PatientResponseDto.fromEntity(patient);
    }

    @Override
    @Transactional
    public PatientResponseDto updatePatient(Long id, Long userId, PatientRequestDto request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));

        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setPhone(request.getPhone());
        patient.setEmail(request.getEmail());
        patient.setAddress(request.getAddress());
        patient.setCity(request.getCity());
        patient.setState(request.getState());
        patient.setPostalCode(request.getPostalCode());
        patient.setBloodGroup(request.getBloodGroup());
        patient.setAllergies(request.getAllergies());
        patient.setMedicalHistory(request.getMedicalHistory());
        patient.setEmergencyContactName(request.getEmergencyContactName());
        patient.setEmergencyContactPhone(request.getEmergencyContactPhone());

        Patient updated = patientRepository.save(patient);
        return PatientResponseDto.fromEntity(updated);
    }

    @Override
    @Transactional
    public void deletePatient(Long id, Long userId) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));
        patientRepository.delete(patient);
    }

    @Override
    public PatientResponseDto getPatientByUserId(Long userId) {
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile", "userId", userId));
        return PatientResponseDto.fromEntity(patient);
    }
}
