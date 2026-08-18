package com.mediconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticsDto {

    private long totalUsers;
    private long totalPatients;
    private long totalDoctors;
    private long totalAppointments;
    private long completedAppointments;
    private long cancelledAppointments;
}
