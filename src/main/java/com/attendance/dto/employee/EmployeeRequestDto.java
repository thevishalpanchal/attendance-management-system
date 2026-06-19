package com.attendance.dto.employee;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequestDto {

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String password;

    private Long departmentId;

    private Long roleId;

    private String designation;

    private LocalDate joiningDate;

    private BigDecimal monthlySalary;
}