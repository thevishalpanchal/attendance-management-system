package com.attendance.dto.attendance;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceResponseDto {

    private Long id;

    private Long employeeId;

    private String employeeName;

    private LocalDate attendanceDate;

    private String status;

    private LocalDateTime markedAt;
    
    

}