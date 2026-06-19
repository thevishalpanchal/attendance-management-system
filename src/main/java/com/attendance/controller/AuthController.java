package com.attendance.controller;

import com.attendance.dto.auth.LoginRequestDto;
import com.attendance.dto.auth.LoginResponseDto;
import com.attendance.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(
            AuthService authService) {

        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto>
    login(
            @RequestBody
            LoginRequestDto requestDto) {

        return ResponseEntity.ok(
                authService.login(
                        requestDto));
    }
}