package com.mediconnect.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Specialization specialization;

    @Column(nullable = false, length = 100)
    private String qualification;

    @Column(nullable = false)
    private int experienceYears;

    @Column(nullable = false, unique = true, length = 50)
    private String licenseNumber;

    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 100)
    private String hospitalName;

    @Column(nullable = false)
    private double consultationFee;

    @Column(nullable = false)
    private boolean available = true;

    @Column(length = 1000)
    private String about;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum Specialization {
        GENERAL_PHYSICIAN,
        CARDIOLOGIST,
        DERMATOLOGIST,
        NEUROLOGIST,
        PEDIATRICIAN,
        ORTHOPEDIC,
        GYNECOLOGIST,
        ENT_SPECIALIST,
        DENTIST,
        PSYCHIATRIST
    }
}
