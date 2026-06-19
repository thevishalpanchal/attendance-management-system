package com.attendance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_policy")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeavePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_day_leave_per_month")
    private BigDecimal fullDayLeavePerMonth;

    @Column(name = "half_day_leave_per_month")
    private BigDecimal halfDayLeavePerMonth;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}