package com.attendance.service;

import com.attendance.dto.auth.LoginRequestDto;
import com.attendance.dto.auth.LoginResponseDto;

public interface AuthService {

    LoginResponseDto login(
            LoginRequestDto requestDto
    );

}