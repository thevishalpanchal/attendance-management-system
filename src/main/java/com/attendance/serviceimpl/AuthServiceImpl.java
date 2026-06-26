package com.attendance.serviceimpl;

import com.attendance.dto.auth.LoginRequestDto;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.attendance.dto.auth.LoginResponseDto;
import com.attendance.entity.Employee;
import com.attendance.exception.ResourceNotFoundException;
import com.attendance.repository.EmployeeRepository;
import com.attendance.security.JwtUtil;
import com.attendance.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
	
	private final PasswordEncoder passwordEncoder;

    private final EmployeeRepository employeeRepository;

    private final JwtUtil jwtUtil;
    
    

    public AuthServiceImpl(
            EmployeeRepository employeeRepository,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder) {

        this.employeeRepository = employeeRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public LoginResponseDto login(
            LoginRequestDto requestDto) {

        Employee employee =
                employeeRepository
                        .findByEmail(
                                requestDto.getEmail())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Invalid Email"));


		System.out.println("EMAIL = " + requestDto.getEmail());
		System.out.println("DB EMAIL = " + employee.getEmail());
		System.out.println("PASSWORD MATCH = " +
        passwordEncoder.matches(
                requestDto.getPassword(),
                employee.getPassword()));

        if (!passwordEncoder.matches(
                requestDto.getPassword(),
                employee.getPassword())) {

        	throw new BadCredentialsException(
        	        "Invalid Email or Password");
        }
        String token =
                jwtUtil.generateToken(
                        employee.getEmail(),
                        employee.getRole()
                                .getRoleName());

        return LoginResponseDto.builder()
                .token(token)
                .email(employee.getEmail())
                .role(
                        employee.getRole()
                                .getRoleName())
                .employeeName(
                        employee.getFirstName()
                                + " "
                                + employee.getLastName())
                .build();
    }
}
