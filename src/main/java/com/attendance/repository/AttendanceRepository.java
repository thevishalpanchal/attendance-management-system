package com.attendance.repository;

import com.attendance.entity.Attendance;
import com.attendance.entity.Employee;
import com.attendance.enums.AttendanceStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

	Optional<Attendance> findByEmployee_IdAndAttendanceDate(
	        Long employeeId,
	        LocalDate attendanceDate
	);
	
	List<Attendance> findByEmployee_Id(Long employeeId);
	
	List<Attendance> findByAttendanceDate(
	        LocalDate attendanceDate
	);
	
	long countByAttendanceDateAndStatus(
	        LocalDate attendanceDate,
	        AttendanceStatus status
	);
	
	Long countByEmployee_IdAndStatusAndAttendanceDateBetween(
	        Long employeeId,
	        AttendanceStatus status,
	        LocalDate startDate,
	        LocalDate endDate
	);

}