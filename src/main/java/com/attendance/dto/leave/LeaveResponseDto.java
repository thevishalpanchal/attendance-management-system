package com.attendance.dto.leave;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveResponseDto {

    private Long id;

    private Long employeeId;

    private String employeeName;

    private String leaveType;

    private String leaveDuration;

    private LocalDate startDate;

    private LocalDate endDate;

    private String reason;

    private String status;

}