package com.attendance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "leave_balance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer month;

    private Integer year;

    @Column(name = "total_full_day_leave")
    private BigDecimal totalFullDayLeave;

    @Column(name = "used_full_day_leave")
    private BigDecimal usedFullDayLeave;

    @Column(name = "total_half_day_leave")
    private BigDecimal totalHalfDayLeave;

    @Column(name = "used_half_day_leave")
    private BigDecimal usedHalfDayLeave;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;
}