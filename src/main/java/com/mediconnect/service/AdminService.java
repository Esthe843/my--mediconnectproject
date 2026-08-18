package com.mediconnect.service;

import com.mediconnect.dto.StatisticsDto;
import com.mediconnect.dto.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {

    Page<UserResponseDto> getAllUsers(Pageable pageable);

    UserResponseDto getUserById(Long id);

    UserResponseDto toggleUserEnabled(Long id);

    void deleteUser(Long id);

    StatisticsDto getStatistics();
}
