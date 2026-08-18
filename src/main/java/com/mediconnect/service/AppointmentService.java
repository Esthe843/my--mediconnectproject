package com.mediconnect.service;

import com.mediconnect.dto.AppointmentRequestDto;
import com.mediconnect.dto.AppointmentResponseDto;

import java.util.List;

public interface AppointmentService {

    AppointmentResponseDto bookAppointment(AppointmentRequestDto request);

    AppointmentResponseDto getAppointmentById(Long id);

    List<AppointmentResponseDto> getAppointmentsByPatient(Long patientId);

    List<AppointmentResponseDto> getAppointmentsByDoctor(Long doctorId);

    AppointmentResponseDto confirmAppointment(Long id);

    AppointmentResponseDto cancelAppointment(Long id);

    AppointmentResponseDto completeAppointment(Long id);

    AppointmentResponseDto rescheduleAppointment(Long id, AppointmentRequestDto request);
}
