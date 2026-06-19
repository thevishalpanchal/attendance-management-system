package com.attendance.repository;

import com.attendance.entity.LeavePolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeavePolicyRepository
        extends JpaRepository<LeavePolicy, Long> {
}