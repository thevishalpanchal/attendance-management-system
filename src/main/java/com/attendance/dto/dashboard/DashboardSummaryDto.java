package com.attendance.dto.dashboard;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardSummaryDto {

    private Long totalEmployees;

    private Long activeEmployees;

    private Long presentToday;

    private Long leaveToday;

    private Long halfDayToday;

    private Long pendingLeaves;
}