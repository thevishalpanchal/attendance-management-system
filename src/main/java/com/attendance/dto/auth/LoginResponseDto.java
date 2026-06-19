package com.attendance.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {

    private String token;

    private String role;

    private String employeeName;
    
    private String email;
}