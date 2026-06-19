package com.attendance.security;

import com.attendance.entity.Employee;
import com.attendance.repository.EmployeeRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public CustomUserDetailsService(
            EmployeeRepository employeeRepository) {

        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(
            String email)
            throws UsernameNotFoundException {

        Employee employee =
                employeeRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "Employee not found"));

        return new User(
                employee.getEmail(),
                employee.getPassword(),
                List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_"
                                        + employee.getRole()
                                        .getRoleName()))
        );
    }
}