package com.attendance.config;

import com.attendance.entity.*;
import com.attendance.enums.EmployeeStatus;
import com.attendance.enums.LeaveTypeStatus;
import com.attendance.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer
        implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeavePolicyRepository leavePolicyRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args)
            throws Exception {

        createRoles();

        createDepartments();

        createLeaveTypes();

        createLeavePolicy();

        createAdminUser();

        System.out.println(
                "Default data initialized successfully.");
    }

    private void createRoles() {

        if (roleRepository.count() == 0) {

            roleRepository.save(
                    Role.builder()
                            .roleName("ADMIN")
                            .description("System Administrator")
                            .build());

            roleRepository.save(
                    Role.builder()
                            .roleName("HR")
                            .description("Human Resource")
                            .build());

            roleRepository.save(
                    Role.builder()
                            .roleName("EMPLOYEE")
                            .description("Employee")
                            .build());

            System.out.println("Roles created.");
        }
    }

    private void createDepartments() {

        if (departmentRepository.count() == 0) {

            departmentRepository.save(
                    Department.builder()
                            .departmentName("IT")
                            .description("Information Technology")
                            .status("ACTIVE")
                            .createdAt(LocalDateTime.now())
                            .build());

            departmentRepository.save(
                    Department.builder()
                            .departmentName("HR")
                            .description("Human Resources")
                            .status("ACTIVE")
                            .createdAt(LocalDateTime.now())
                            .build());

            departmentRepository.save(
                    Department.builder()
                            .departmentName("Finance")
                            .description("Finance Department")
                            .status("ACTIVE")
                            .createdAt(LocalDateTime.now())
                            .build());

            departmentRepository.save(
                    Department.builder()
                            .departmentName("Sales")
                            .description("Sales Department")
                            .status("ACTIVE")
                            .createdAt(LocalDateTime.now())
                            .build());

            System.out.println("Departments created.");
        }
    }

    private void createLeaveTypes() {

        if (leaveTypeRepository.count() == 0) {

            leaveTypeRepository.save(
                    LeaveType.builder()
                            .leaveName("Casual Leave")
                            .isPaid(true)
                            .status(LeaveTypeStatus.ACTIVE)
                            .createdAt(LocalDateTime.now())
                            .build());

            leaveTypeRepository.save(
                    LeaveType.builder()
                            .leaveName("Sick Leave")
                            .isPaid(true)
                            .status(LeaveTypeStatus.ACTIVE)
                            .createdAt(LocalDateTime.now())
                            .build());

            leaveTypeRepository.save(
                    LeaveType.builder()
                            .leaveName("Earned Leave")
                            .isPaid(true)
                            .status(LeaveTypeStatus.ACTIVE)
                            .createdAt(LocalDateTime.now())
                            .build());

            System.out.println("Leave types created.");
        }
    }

    private void createLeavePolicy() {

        if (leavePolicyRepository.count() == 0) {

            LeavePolicy leavePolicy =
                    LeavePolicy.builder()
                            .fullDayLeavePerMonth(
                                    BigDecimal.ONE)
                            .halfDayLeavePerMonth(
                                    BigDecimal.ONE)
                            .createdAt(
                                    LocalDateTime.now())
                            .build();

            leavePolicyRepository.save(
                    leavePolicy);

            System.out.println(
                    "Leave policy created.");
        }
    }

    private void createAdminUser() {

        if (employeeRepository
                .findByEmail("admin@bse.com")
                .isEmpty()) {

            Role adminRole =
                    roleRepository
                            .findByRoleName(
                                    "ADMIN")
                            .orElseThrow();

            Department department =
                    departmentRepository
                            .findByDepartmentName(
                                    "IT")
                            .orElseThrow();

            Employee admin =
                    Employee.builder()
                            .employeeCode("ADMIN001")
                            .firstName("System")
                            .lastName("Admin")
                            .email("admin@bse.com")
                            .password(
                                    passwordEncoder.encode(
                                            "Admin@123"))
                            .designation(
                                    "Administrator")
                            .department(
                                    department)
                            .role(
                                    adminRole)
                            .status(
                                    EmployeeStatus.ACTIVE)
                            .createdAt(
                                    LocalDateTime.now())
                            .updatedAt(
                                    LocalDateTime.now())
                            .build();

            employeeRepository.save(
                    admin);

            System.out.println(
                    "Default Admin created.");
        }
    }
}