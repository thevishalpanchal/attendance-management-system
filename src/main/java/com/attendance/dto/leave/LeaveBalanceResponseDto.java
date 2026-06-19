package com.attendance.dto.leave;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveBalanceResponseDto {

    private Long employeeId;

    private String employeeName;

    private BigDecimal totalFullDayLeave;

    private BigDecimal usedFullDayLeave;

    private BigDecimal remainingFullDayLeave;

    private BigDecimal totalHalfDayLeave;

    private BigDecimal usedHalfDayLeave;

    private BigDecimal remainingHalfDayLeave;
}