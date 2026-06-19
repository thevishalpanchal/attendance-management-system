package com.attendance.repository;

import com.attendance.entity.Employee;
import com.attendance.enums.EmployeeStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByEmployeeCode(String employeeCode);

    boolean existsByEmail(String email);

    boolean existsByEmployeeCode(String employeeCode);
    
    List<Employee> findByStatus(EmployeeStatus status);
    
    long countByStatus(EmployeeStatus status);
    
    List<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String firstName,
            String lastName,
            String email
    );
    
    List<Employee> findByDepartment_Id(
            Long departmentId);

}