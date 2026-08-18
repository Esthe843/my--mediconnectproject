package com.mediconnect.service.impl;

import com.mediconnect.dto.AppointmentRequestDto;
import com.mediconnect.dto.AppointmentResponseDto;
import com.mediconnect.entity.Appointment;
import com.mediconnect.entity.Doctor;
import com.mediconnect.entity.Patient;
import com.mediconnect.exception.BadRequestException;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.AppointmentRepository;
import com.mediconnect.repository.DoctorRepository;
import com.mediconnect.repository.PatientRepository;
import com.mediconnect.service.AppointmentService;
import com.mediconnect.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public AppointmentResponseDto bookAppointment(AppointmentRequestDto request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", request.getPatientId()));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", request.getDoctorId()));

        // Business rule: no double-booking
        if (appointmentRepository.existsConflictingAppointment(
                doctor.getId(), request.getAppointmentDate(), request.getAppointmentTime())) {
            throw new BadRequestException("Doctor already has an appointment at this date and time");
        }

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .appointmentType(request.getAppointmentType())
                .reason(request.getReason())
                .notes(request.getNotes())
                .status(Appointment.AppointmentStatus.PENDING)
                .build();

        Appointment saved = appointmentRepository.save(appointment);

        // Notification stub
        notificationService.notify(
                patient.getUser().getId(),
                "APPOINTMENT_BOOKED",
                "Appointment Booked",
                "Your appointment with Dr. " + doctor.getFirstName() + " " + doctor.getLastName()
                        + " is booked for " + request.getAppointmentDate() + " at " + request.getAppointmentTime()
        );

        return AppointmentResponseDto.fromEntity(saved);
    }

    @Override
    public AppointmentResponseDto getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", id));
        return AppointmentResponseDto.fromEntity(appointment);
    }

    @Override
    public List<AppointmentResponseDto> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(AppointmentResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponseDto> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(AppointmentResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AppointmentResponseDto confirmAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", id));

        if (appointment.getStatus() == Appointment.AppointmentStatus.CANCELLED) {
            throw new BadRequestException("Cannot confirm a cancelled appointment");
        }

        appointment.setStatus(Appointment.AppointmentStatus.CONFIRMED);
        Appointment saved = appointmentRepository.save(appointment);

        notificationService.notify(
                appointment.getPatient().getUser().getId(),
                "APPOINTMENT_CONFIRMED",
                "Appointment Confirmed",
                "Your appointment with Dr. " + appointment.getDoctor().getFirstName()
                        + " has been confirmed for " + appointment.getAppointmentDate()
        );

        return AppointmentResponseDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public AppointmentResponseDto cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", id));

        if (appointment.getStatus() == Appointment.AppointmentStatus.COMPLETED) {
            throw new BadRequestException("Cannot cancel a completed appointment");
        }

        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        Appointment saved = appointmentRepository.save(appointment);

        notificationService.notify(
                appointment.getDoctor().getUser().getId(),
                "APPOINTMENT_CANCELLED",
                "Appointment Cancelled",
                "Appointment with patient " + appointment.getPatient().getFirstName()
                        + " on " + appointment.getAppointmentDate() + " has been cancelled"
        );

        return AppointmentResponseDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public AppointmentResponseDto completeAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", id));

        if (appointment.getStatus() == Appointment.AppointmentStatus.CANCELLED) {
            throw new BadRequestException("Cannot complete a cancelled appointment");
        }

        appointment.setStatus(Appointment.AppointmentStatus.COMPLETED);
        Appointment saved = appointmentRepository.save(appointment);
        return AppointmentResponseDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public AppointmentResponseDto rescheduleAppointment(Long id, AppointmentRequestDto request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", id));

        if (appointment.getStatus() == Appointment.AppointmentStatus.CANCELLED) {
            throw new BadRequestException("Cannot reschedule a cancelled appointment");
        }
        if (appointment.getStatus() == Appointment.AppointmentStatus.COMPLETED) {
            throw new BadRequestException("Cannot reschedule a completed appointment");
        }

        // Check for conflicts at new time
        if (appointmentRepository.existsConflictingAppointment(
                appointment.getDoctor().getId(), request.getAppointmentDate(), request.getAppointmentTime())) {
            throw new BadRequestException("Doctor already has an appointment at this date and time");
        }

        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setStatus(Appointment.AppointmentStatus.RESCHEDULED);
        Appointment saved = appointmentRepository.save(appointment);
        return AppointmentResponseDto.fromEntity(saved);
    }
}
