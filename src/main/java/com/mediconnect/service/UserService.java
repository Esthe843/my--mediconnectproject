package com.mediconnect.service;

import com.mediconnect.dto.LoginRequestDto;
import com.mediconnect.dto.RegisterRequestDto;
import com.mediconnect.dto.UserResponseDto;

public interface UserService {

    UserResponseDto register(RegisterRequestDto request);

    UserResponseDto login(LoginRequestDto request);

    UserResponseDto getProfile(Long userId);
}
