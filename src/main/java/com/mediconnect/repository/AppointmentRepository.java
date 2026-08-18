package com.mediconnect.repository;

import com.mediconnect.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDoctorId(Long doctorId);

    List<Appointment> findByPatientIdAndStatus(Long patientId, Appointment.AppointmentStatus status);

    List<Appointment> findByDoctorIdAndStatus(Long doctorId, Appointment.AppointmentStatus status);

    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE " +
           "a.doctor.id = :doctorId AND " +
           "a.appointmentDate = :date AND " +
           "a.appointmentTime = :time AND " +
           "a.status <> com.mediconnect.entity.Appointment$AppointmentStatus.CANCELLED")
    boolean existsConflictingAppointment(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time);
}
