package com.attendance.serviceimpl;

import com.attendance.dto.leave.LeaveApplyRequestDto;
import com.attendance.dto.leave.LeaveBalanceResponseDto;
import com.attendance.dto.leave.LeaveResponseDto;
import com.attendance.dto.leave.LeaveSummaryDto;
import com.attendance.entity.Attendance;
import com.attendance.entity.Employee;
import com.attendance.entity.LeaveBalance;
import com.attendance.entity.LeaveRequest;
import com.attendance.entity.LeaveType;
import com.attendance.enums.AttendanceStatus;
import com.attendance.enums.LeaveDuration;
import com.attendance.enums.LeaveRequestStatus;
import com.attendance.exception.ResourceNotFoundException;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.EmployeeRepository;
import com.attendance.repository.LeaveBalanceRepository;
import com.attendance.repository.LeaveRequestRepository;
import com.attendance.repository.LeaveTypeRepository;
import com.attendance.service.LeaveService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeaveServiceImpl implements LeaveService {

	private final LeaveRequestRepository leaveRequestRepository;

	private final EmployeeRepository employeeRepository;

	private final LeaveTypeRepository leaveTypeRepository;

	private final AttendanceRepository attendanceRepository;

	private final LeaveBalanceRepository leaveBalanceRepository;

	public LeaveServiceImpl(LeaveRequestRepository leaveRequestRepository, EmployeeRepository employeeRepository,
			LeaveTypeRepository leaveTypeRepository, AttendanceRepository attendanceRepository,
			LeaveBalanceRepository leaveBalanceRepository) {

		this.leaveRequestRepository = leaveRequestRepository;
		this.employeeRepository = employeeRepository;
		this.leaveTypeRepository = leaveTypeRepository;
		this.attendanceRepository = attendanceRepository;
		this.leaveBalanceRepository = leaveBalanceRepository;
	}

	@Override
	public LeaveResponseDto applyLeave(LeaveApplyRequestDto requestDto) {

		Employee employee = employeeRepository.findById(requestDto.getEmployeeId())
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

		LeaveType leaveType = leaveTypeRepository.findById(requestDto.getLeaveTypeId())
				.orElseThrow(() -> new ResourceNotFoundException("Leave Type not found"));

		LeaveRequest leaveRequest = LeaveRequest.builder().employee(employee).leaveType(leaveType)
				.leaveDuration(requestDto.getLeaveDuration()).startDate(requestDto.getStartDate())
				.endDate(requestDto.getEndDate()).reason(requestDto.getReason()).status(LeaveRequestStatus.PENDING)
				.createdAt(LocalDateTime.now()).build();

		LeaveRequest savedLeave = leaveRequestRepository.save(leaveRequest);

		return LeaveResponseDto.builder().id(savedLeave.getId()).employeeId(employee.getId())
				.employeeName(employee.getFirstName() + " " + employee.getLastName())
				.leaveType(leaveType.getLeaveName()).leaveDuration(savedLeave.getLeaveDuration().name())
				.startDate(savedLeave.getStartDate()).endDate(savedLeave.getEndDate()).reason(savedLeave.getReason())
				.status(savedLeave.getStatus().name()).build();
	}

	@Override
	public List<LeaveResponseDto> getPendingLeaves() {

		return leaveRequestRepository.findByStatus(LeaveRequestStatus.PENDING).stream().map(this::mapToResponse)
				.toList();
	}

	@Override
	public List<LeaveResponseDto> getLeavesByEmployeeId(Long employeeId) {

		return leaveRequestRepository.findByEmployee_Id(employeeId).stream().map(this::mapToResponse).toList();
	}

	@Override
	public LeaveResponseDto getLeaveById(Long leaveId) {

		LeaveRequest leave = leaveRequestRepository.findById(leaveId)
				.orElseThrow(() -> new ResourceNotFoundException("Leave not found"));

		return mapToResponse(leave);
	}

	@Override
	public void approveLeave(Long leaveId, Long approvedBy) {

		LeaveRequest leave = leaveRequestRepository.findById(leaveId)
				.orElseThrow(() -> new ResourceNotFoundException("Leave not found"));

		leave.setStatus(LeaveRequestStatus.APPROVED);

		leave.setApprovedBy(approvedBy);

		Attendance attendance = attendanceRepository
				.findByEmployee_IdAndAttendanceDate(leave.getEmployee().getId(), leave.getStartDate()).orElse(null);

		if (attendance != null) {

			if (leave.getLeaveDuration() == LeaveDuration.HALF_DAY) {

				attendance.setStatus(AttendanceStatus.HALF_DAY);

			} else {

				attendance.setStatus(AttendanceStatus.LEAVE);
			}

			attendanceRepository.save(attendance);
		}

		LeaveBalance leaveBalance = leaveBalanceRepository.findByEmployee_IdAndMonthAndYear(leave.getEmployee().getId(),
				leave.getStartDate().getMonthValue(), leave.getStartDate().getYear()).orElse(null);

		if (leaveBalance != null) {

			if (leave.getLeaveDuration() == LeaveDuration.HALF_DAY) {

				leaveBalance.setUsedHalfDayLeave(leaveBalance.getUsedHalfDayLeave().add(new BigDecimal("0.5")));

			} else {

				leaveBalance.setUsedFullDayLeave(leaveBalance.getUsedFullDayLeave().add(BigDecimal.ONE));
			}

			leaveBalanceRepository.save(leaveBalance);
		}

		leaveRequestRepository.save(leave);
	}

	@Override
	public void rejectLeave(Long leaveId) {

		LeaveRequest leave = leaveRequestRepository.findById(leaveId)
				.orElseThrow(() -> new ResourceNotFoundException("Leave not found"));

		leave.setStatus(LeaveRequestStatus.REJECTED);

		leaveRequestRepository.save(leave);
	}

	@Override
	public List<LeaveResponseDto> getApprovedLeaves() {

		return leaveRequestRepository.findByStatus(LeaveRequestStatus.APPROVED).stream().map(this::mapToResponse)
				.toList();
	}

	@Override
	public List<LeaveResponseDto> getRejectedLeaves() {

		return leaveRequestRepository.findByStatus(LeaveRequestStatus.REJECTED).stream().map(this::mapToResponse)
				.toList();
	}

	
	
	@Override
	public LeaveBalanceResponseDto
	getMyLeaveBalance() {

	    Authentication authentication =
	            SecurityContextHolder
	                    .getContext()
	                    .getAuthentication();

	    String email =
	            authentication.getName();

	    Employee employee =
	            employeeRepository
	                    .findByEmail(email)
	                    .orElseThrow(() ->
	                            new ResourceNotFoundException(
	                                    "Employee not found"));

	    LocalDate today = LocalDate.now();
	    
	    System.out.println("Employee ID : " + employee.getId());
	    System.out.println("Month : " + today.getMonthValue());
	    System.out.println("Year : " + today.getYear());

	    LeaveBalance leaveBalance =
	            leaveBalanceRepository
	                    .findByEmployee_IdAndMonthAndYear(
	                            employee.getId(),
	                            today.getMonthValue(),
	                            today.getYear())
	                    .orElseThrow(() ->
	                            new ResourceNotFoundException(
	                                    "Leave balance not found"));

	    return LeaveBalanceResponseDto.builder()
	            .employeeId(employee.getId())
	            .employeeName(
	                    employee.getFirstName()
	                            + " "
	                            + employee.getLastName())
	            .totalFullDayLeave(
	                    leaveBalance.getTotalFullDayLeave())
	            .usedFullDayLeave(
	                    leaveBalance.getUsedFullDayLeave())
	            .remainingFullDayLeave(
	                    leaveBalance.getTotalFullDayLeave()
	                            .subtract(
	                                    leaveBalance.getUsedFullDayLeave()))
	            .totalHalfDayLeave(
	                    leaveBalance.getTotalHalfDayLeave())
	            .usedHalfDayLeave(
	                    leaveBalance.getUsedHalfDayLeave())
	            .remainingHalfDayLeave(
	                    leaveBalance.getTotalHalfDayLeave()
	                            .subtract(
	                                    leaveBalance.getUsedHalfDayLeave()))
	            .build();
	}
	
	
	@Override
	public List<LeaveResponseDto> getMyLeaves() {

	    Authentication authentication =
	            SecurityContextHolder
	                    .getContext()
	                    .getAuthentication();

	    String email =
	            authentication.getName();

	    Employee employee =
	            employeeRepository
	                    .findByEmail(email)
	                    .orElseThrow(() ->
	                            new ResourceNotFoundException(
	                                    "Employee not found"));

	    return leaveRequestRepository
	            .findByEmployee_Id(employee.getId())
	            .stream()
	            .map(this::mapToResponse)
	            .toList();
	}
	
	@Override
	public LeaveSummaryDto getLeaveSummary() {

	    Long pendingLeaves =
	            leaveRequestRepository.countByStatus(
	                    LeaveRequestStatus.PENDING);

	    Long approvedLeaves =
	            leaveRequestRepository.countByStatus(
	                    LeaveRequestStatus.APPROVED);

	    Long rejectedLeaves =
	            leaveRequestRepository.countByStatus(
	                    LeaveRequestStatus.REJECTED);

	    return LeaveSummaryDto.builder()
	            .pendingLeaves(pendingLeaves)
	            .approvedLeaves(approvedLeaves)
	            .rejectedLeaves(rejectedLeaves)
	            .build();
	}
	
	@Override
	public LeaveBalanceResponseDto
	getLeaveBalanceByEmployeeId(
	        Long employeeId) {

	    Employee employee =
	            employeeRepository
	                    .findById(employeeId)
	                    .orElseThrow(() ->
	                            new ResourceNotFoundException(
	                                    "Employee not found"));

	    LocalDate today =
	            LocalDate.now();

	    LeaveBalance leaveBalance =
	            leaveBalanceRepository
	                    .findByEmployee_IdAndMonthAndYear(
	                            employeeId,
	                            today.getMonthValue(),
	                            today.getYear())
	                    .orElseThrow(() ->
	                            new ResourceNotFoundException(
	                                    "Leave balance not found"));

	    return LeaveBalanceResponseDto.builder()
	            .employeeId(employee.getId())
	            .employeeName(
	                    employee.getFirstName()
	                            + " "
	                            + employee.getLastName())
	            .totalFullDayLeave(
	                    leaveBalance.getTotalFullDayLeave())
	            .usedFullDayLeave(
	                    leaveBalance.getUsedFullDayLeave())
	            .remainingFullDayLeave(
	                    leaveBalance.getTotalFullDayLeave()
	                            .subtract(
	                                    leaveBalance.getUsedFullDayLeave()))
	            .totalHalfDayLeave(
	                    leaveBalance.getTotalHalfDayLeave())
	            .usedHalfDayLeave(
	                    leaveBalance.getUsedHalfDayLeave())
	            .remainingHalfDayLeave(
	                    leaveBalance.getTotalHalfDayLeave()
	                            .subtract(
	                                    leaveBalance.getUsedHalfDayLeave()))
	            .build();
	}
	
	private LeaveResponseDto mapToResponse(LeaveRequest leave) {

		return LeaveResponseDto.builder().id(leave.getId()).employeeId(leave.getEmployee().getId())
				.employeeName(leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName())
				.leaveType(leave.getLeaveType().getLeaveName()).leaveDuration(leave.getLeaveDuration().name())
				.startDate(leave.getStartDate()).endDate(leave.getEndDate()).reason(leave.getReason())
				.status(leave.getStatus().name()).build();
	}
	
	
}