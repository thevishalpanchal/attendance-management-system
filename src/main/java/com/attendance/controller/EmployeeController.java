package com.attendance.controller;

import com.attendance.dto.employee.EmployeeRequestDto;
import com.attendance.dto.employee.EmployeeResponseDto;
import com.attendance.service.EmployeeService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(
            @RequestBody EmployeeRequestDto requestDto) {

        EmployeeResponseDto response = employeeService.createEmployee(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @GetMapping
    public ResponseEntity<List<EmployeeResponseDto>> getAllEmployees() {

        return ResponseEntity.ok(
                employeeService.getAllEmployees()
        );
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                employeeService.getEmployeeById(id)
        );
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeRequestDto requestDto) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(id, requestDto)
        );
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateEmployee(
            @PathVariable Long id) {

        employeeService.deactivateEmployee(id);

        return ResponseEntity.ok(
                "Employee deactivated successfully");
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<EmployeeResponseDto>> getActiveEmployees() {

        return ResponseEntity.ok(
                employeeService.getActiveEmployees()
        );
    }
    
    
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeResponseDto>>
    searchEmployees(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                employeeService.searchEmployees(
                        keyword));
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<EmployeeResponseDto>>
    getEmployeesByDepartment(
            @PathVariable Long departmentId) {

        return ResponseEntity.ok(
                employeeService.getEmployeesByDepartment(
                        departmentId));
    }
}