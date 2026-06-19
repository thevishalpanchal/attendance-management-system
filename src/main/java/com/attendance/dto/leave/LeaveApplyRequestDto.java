package com.attendance.dto.leave;

import com.attendance.enums.LeaveDuration;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveApplyRequestDto {

    private Long employeeId;

    private Long leaveTypeId;

    private LeaveDuration leaveDuration;

    private LocalDate startDate;

    private LocalDate endDate;

    private String reason;

}