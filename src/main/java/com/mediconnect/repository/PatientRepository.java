package com.mediconnect.repository;

import com.mediconnect.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
