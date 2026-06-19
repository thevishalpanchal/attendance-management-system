package com.attendance.dto.employee;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponseDto {

    private Long id;

    private String employeeCode;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String departmentName;

    private String roleName;

    private String designation;

    private LocalDate joiningDate;

    private BigDecimal monthlySalary;

    private String status;
}