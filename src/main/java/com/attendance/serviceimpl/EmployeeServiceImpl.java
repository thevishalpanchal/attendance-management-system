package com.attendance.serviceimpl;

import com.attendance.dto.employee.EmployeeRequestDto;
import java.time.LocalDateTime;

import com.attendance.exception.DuplicateResourceException;
import com.attendance.exception.ResourceNotFoundException;
import com.attendance.dto.employee.EmployeeResponseDto;
import com.attendance.entity.Department;
import com.attendance.entity.Employee;
import com.attendance.entity.Role;
import com.attendance.enums.EmployeeStatus;
import com.attendance.repository.DepartmentRepository;
import com.attendance.repository.EmployeeRepository;
import com.attendance.repository.RoleRepository;
import com.attendance.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

private final EmployeeRepository employeeRepository;

private final DepartmentRepository departmentRepository;

private final RoleRepository roleRepository;

private final PasswordEncoder passwordEncoder;

public EmployeeServiceImpl(
        EmployeeRepository employeeRepository,
        DepartmentRepository departmentRepository,
        RoleRepository roleRepository,
        PasswordEncoder passwordEncoder) {

    this.employeeRepository = employeeRepository;
    this.departmentRepository = departmentRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
}

@Override
public EmployeeResponseDto createEmployee(EmployeeRequestDto requestDto) {

    if (employeeRepository.existsByEmail(requestDto.getEmail())) {
        throw new DuplicateResourceException("Email already exists");
    }

    Department department = departmentRepository
            .findById(requestDto.getDepartmentId())
            .orElseThrow(() ->
                    new ResourceNotFoundException("Department not found"));

    Role role = roleRepository
            .findById(requestDto.getRoleId())
            .orElseThrow(() ->
                    new ResourceNotFoundException("Role not found"));

    String employeeCode = "EMP" + String.format("%03d",
            employeeRepository.count() + 1);

    Employee employee = Employee.builder()
            .employeeCode(employeeCode)
            .firstName(requestDto.getFirstName())
            .lastName(requestDto.getLastName())
            .email(requestDto.getEmail())
            .phone(requestDto.getPhone())
            .password(passwordEncoder.encode(requestDto.getPassword()))
            .designation(requestDto.getDesignation())
            .joiningDate(requestDto.getJoiningDate())
            .monthlySalary(requestDto.getMonthlySalary())
            .status(EmployeeStatus.ACTIVE)
            .department(department)
            .role(role)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    Employee savedEmployee = employeeRepository.save(employee);

    return EmployeeResponseDto.builder()
            .id(savedEmployee.getId())
            .employeeCode(savedEmployee.getEmployeeCode())
            .firstName(savedEmployee.getFirstName())
            .lastName(savedEmployee.getLastName())
            .email(savedEmployee.getEmail())
            .phone(savedEmployee.getPhone())
            .departmentName(savedEmployee.getDepartment().getDepartmentName())
            .roleName(savedEmployee.getRole().getRoleName())
            .designation(savedEmployee.getDesignation())
            .joiningDate(savedEmployee.getJoiningDate())
            .monthlySalary(savedEmployee.getMonthlySalary())
            .status(savedEmployee.getStatus().name())
            .build();
}

@Override
public EmployeeResponseDto getEmployeeById(Long employeeId) {

    Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                    new ResourceNotFoundException("Employee not found with id : " + employeeId));

    return EmployeeResponseDto.builder().id(employee.getId())
            .employeeCode(employee.getEmployeeCode())
            .firstName(employee.getFirstName())
            .lastName(employee.getLastName())
            .email(employee.getEmail())
            .phone(employee.getPhone())
            .departmentName(employee.getDepartment().getDepartmentName())
            .roleName(employee.getRole().getRoleName())
            .designation(employee.getDesignation())
            .joiningDate(employee.getJoiningDate())
            .monthlySalary(employee.getMonthlySalary())
            .status(employee.getStatus().name())
            .build();
}

@Override
public List<EmployeeResponseDto> getAllEmployees() {

    List<Employee> employees = employeeRepository.findAll();

    return employees.stream().map(employee -> EmployeeResponseDto.builder()
                    .id(employee.getId())
                    .employeeCode(employee.getEmployeeCode())
                    .firstName(employee.getFirstName())
                    .lastName(employee.getLastName())
                    .email(employee.getEmail())
                    .phone(employee.getPhone())
                    .departmentName(employee.getDepartment().getDepartmentName())
                    .roleName(employee.getRole().getRoleName())
                    .designation(employee.getDesignation())
                    .joiningDate(employee.getJoiningDate())
                    .monthlySalary(employee.getMonthlySalary())
                    .status(employee.getStatus().name())
                    .build())
            .toList();
}

@Override
public EmployeeResponseDto updateEmployee(Long employeeId,
                                          EmployeeRequestDto requestDto) {

    Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Employee not found with id : " + employeeId));

    Department department = departmentRepository
            .findById(requestDto.getDepartmentId())
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Department not found"));

    Role role = roleRepository
            .findById(requestDto.getRoleId())
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Role not found"));

    employee.setFirstName(requestDto.getFirstName());
    employee.setLastName(requestDto.getLastName());
    employee.setEmail(requestDto.getEmail());
    employee.setPhone(requestDto.getPhone());
    employee.setPassword(requestDto.getPassword());
    employee.setDesignation(requestDto.getDesignation());
    employee.setJoiningDate(requestDto.getJoiningDate());
    employee.setMonthlySalary(requestDto.getMonthlySalary());

    employee.setDepartment(department);
    employee.setRole(role);

    employee.setUpdatedAt(LocalDateTime.now());

    Employee updatedEmployee = employeeRepository.save(employee);

    return EmployeeResponseDto.builder()
            .id(updatedEmployee.getId())
            .employeeCode(updatedEmployee.getEmployeeCode())
            .firstName(updatedEmployee.getFirstName())
            .lastName(updatedEmployee.getLastName())
            .email(updatedEmployee.getEmail())
            .phone(updatedEmployee.getPhone())
            .departmentName(updatedEmployee.getDepartment().getDepartmentName())
            .roleName(updatedEmployee.getRole().getRoleName())
            .designation(updatedEmployee.getDesignation())
            .joiningDate(updatedEmployee.getJoiningDate())
            .monthlySalary(updatedEmployee.getMonthlySalary())
            .status(updatedEmployee.getStatus().name())
            .build();
}

@Override
public void deactivateEmployee(Long employeeId) {

    Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Employee not found with id : " + employeeId));

    employee.setStatus(EmployeeStatus.INACTIVE);

    employee.setUpdatedAt(LocalDateTime.now());

    employeeRepository.save(employee);
}

@Override
public List<EmployeeResponseDto> getActiveEmployees() {

    List<Employee> employees =
            employeeRepository.findByStatus(EmployeeStatus.ACTIVE);

    return employees.stream()
            .map(employee -> EmployeeResponseDto.builder()
                    .id(employee.getId())
                    .employeeCode(employee.getEmployeeCode())
                    .firstName(employee.getFirstName())
                    .lastName(employee.getLastName())
                    .email(employee.getEmail())
                    .phone(employee.getPhone())
                    .departmentName(employee.getDepartment().getDepartmentName())
                    .roleName(employee.getRole().getRoleName())
                    .designation(employee.getDesignation())
                    .joiningDate(employee.getJoiningDate())
                    .monthlySalary(employee.getMonthlySalary())
                    .status(employee.getStatus().name())
                    .build())
            .toList();
}

@Override
public List<EmployeeResponseDto> searchEmployees(
        String keyword) {

    return employeeRepository
            .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                    keyword,
                    keyword,
                    keyword)
            .stream()
            .map(this::mapToResponse)
            .toList();
}

@Override
public List<EmployeeResponseDto> getEmployeesByDepartment(
        Long departmentId) {

    return employeeRepository
            .findByDepartment_Id(departmentId)
            .stream()
            .map(this::mapToResponse)
            .toList();
}


private EmployeeResponseDto mapToResponse(
        Employee employee) {

    return EmployeeResponseDto.builder()
            .id(employee.getId())
            .employeeCode(employee.getEmployeeCode())
            .firstName(employee.getFirstName())
            .lastName(employee.getLastName())
            .email(employee.getEmail())
            .phone(employee.getPhone())
            .departmentName(
                    employee.getDepartment()
                            .getDepartmentName())
            .roleName(
                    employee.getRole()
                            .getRoleName())
            .designation(employee.getDesignation())
            .joiningDate(employee.getJoiningDate())
            .monthlySalary(employee.getMonthlySalary())
            .status(employee.getStatus().name())
            .build();
}
}
