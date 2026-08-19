package com.mediconnect.service.impl;

import com.mediconnect.dto.StatisticsDto;
import com.mediconnect.dto.UserResponseDto;
import com.mediconnect.entity.User;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.*;
import com.mediconnect.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserResponseDto::fromEntity);
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return UserResponseDto.fromEntity(user);
    }

    @Override
    @Transactional
    public UserResponseDto toggleUserEnabled(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setEnabled(!user.isEnabled());
        return UserResponseDto.fromEntity(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepository.delete(user);
    }

    @Override
    public StatisticsDto getStatistics() {
        return StatisticsDto.builder()
                .totalUsers(userRepository.count())
                .totalPatients(patientRepository.count())
                .totalDoctors(doctorRepository.count())
                .totalAppointments(appointmentRepository.count())
                .completedAppointments(
                    appointmentRepository.countByStatus(
                        com.mediconnect.entity.Appointment.AppointmentStatus.COMPLETED))
                .cancelledAppointments(
                    appointmentRepository.countByStatus(
                        com.mediconnect.entity.Appointment.AppointmentStatus.CANCELLED))
                .build();
    }
}
