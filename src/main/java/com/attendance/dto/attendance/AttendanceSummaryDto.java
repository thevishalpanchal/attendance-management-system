package com.attendance.dto.attendance;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceSummaryDto {

    private Long totalEmployees;

    private Long present;

    private Long halfDay;

    private Long leave;

    private Long absent;

}