package com.attendance.entity;

import com.attendance.enums.LeaveTypeStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "leave_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "leave_name", nullable = false)
    private String leaveName;

    @Column(name = "is_paid")
    private Boolean isPaid;

    @Enumerated(EnumType.STRING)
    private LeaveTypeStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}