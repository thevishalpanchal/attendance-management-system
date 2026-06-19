package com.attendance.repository;

import com.attendance.entity.LeaveRequest;
import com.attendance.enums.LeaveRequestStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestRepository
        extends JpaRepository<LeaveRequest, Long> {
	
	List<LeaveRequest> findByEmployee_Id(Long employeeId);

	List<LeaveRequest> findByStatus(LeaveRequestStatus status);
	
	Long countByStatus(
	        LeaveRequestStatus status);
	
	Optional<LeaveRequest>
	findByEmployee_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndStatus(
	        Long employeeId,
	        LocalDate startDate,
	        LocalDate endDate,
	        LeaveRequestStatus status
	);
	
	
	
}