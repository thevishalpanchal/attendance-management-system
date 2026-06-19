package com.attendance.repository;

import com.attendance.entity.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeaveBalanceRepository
        extends JpaRepository<LeaveBalance, Long> {
	

    Optional<LeaveBalance> findByEmployee_IdAndMonthAndYear(
            Long employeeId,
            Integer month,
            Integer year
    );
}