package com.attendance.dto.attendance;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyAttendanceReportDto {

    private Long employeeId;

    private String employeeName;

    private Long presentDays;

    private Long leaveDays;

    private Long halfDayDays;

    private Long absentDays;

    private Long weekOffDays;

    private Long holidayDays;
}