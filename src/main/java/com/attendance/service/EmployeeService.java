package com.attendance.service;

import com.attendance.dto.employee.EmployeeRequestDto;
import com.attendance.dto.employee.EmployeeResponseDto;

import java.util.List;

public interface EmployeeService {
	EmployeeResponseDto createEmployee(EmployeeRequestDto requestDto);

	EmployeeResponseDto getEmployeeById(Long employeeId);

	List<EmployeeResponseDto> getAllEmployees();

	EmployeeResponseDto updateEmployee(Long employeeId, EmployeeRequestDto requestDto);

	void deactivateEmployee(Long employeeId);
	
	List<EmployeeResponseDto> getActiveEmployees();
	
	List<EmployeeResponseDto> searchEmployees(
	        String keyword
	);
	
	List<EmployeeResponseDto> getEmployeesByDepartment(
	        Long departmentId
	);

}
