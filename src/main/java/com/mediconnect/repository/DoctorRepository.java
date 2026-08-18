package com.mediconnect.repository;

import com.mediconnect.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByUserId(Long userId);

    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    List<Doctor> findBySpecialization(Doctor.Specialization specialization);

    List<Doctor> findByAvailable(boolean available);

    @Query("SELECT d FROM Doctor d WHERE " +
           "LOWER(d.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.specialization) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.hospitalName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Doctor> searchByKeyword(@Param("keyword") String keyword);

    boolean existsByUserId(Long userId);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByLicenseNumber(String licenseNumber);
}
