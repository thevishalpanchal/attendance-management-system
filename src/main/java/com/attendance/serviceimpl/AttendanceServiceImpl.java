package com.attendance.serviceimpl;

import com.attendance.dto.attendance.AttendanceRequestDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.attendance.dto.attendance.AttendanceResponseDto;
import com.attendance.dto.attendance.AttendanceSummaryDto;
import com.attendance.dto.attendance.MonthlyAttendanceReportDto;
import com.attendance.entity.Attendance;
import com.attendance.entity.Employee;
import com.attendance.enums.AttendanceStatus;
import com.attendance.enums.EmployeeStatus;
import com.attendance.exception.ResourceNotFoundException;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.EmployeeRepository;
import com.attendance.service.AttendanceService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    private final EmployeeRepository employeeRepository;

    public AttendanceServiceImpl(  AttendanceRepository attendanceRepository, EmployeeRepository employeeRepository) {

    this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public AttendanceResponseDto markAttendance( AttendanceRequestDto requestDto) {

        Employee employee = employeeRepository
                .findById(requestDto.getEmployeeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException( "Employee not found"));
        
        if(employee.getStatus() == EmployeeStatus.INACTIVE){
            throw new RuntimeException(
                "Inactive employee cannot mark attendance");
        }
        
        		attendanceRepository.findByEmployee_IdAndAttendanceDate(
                        employee.getId(),
                        LocalDate.now())
                .ifPresent(attendance -> {
                    throw new RuntimeException(
                            "You have already marked attendance for today.");
                });

        Attendance attendance = Attendance.builder()
                .employee(employee)
                .attendanceDate(LocalDate.now())
                .status(AttendanceStatus.PRESENT)
                .markedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        Attendance savedAttendance =
                attendanceRepository.save(attendance);

        return AttendanceResponseDto.builder()
                .id(savedAttendance.getId())
                .employeeId(employee.getId())
                .employeeName(
                        employee.getFirstName()
                                + " "
                                + employee.getLastName())
                .attendanceDate(savedAttendance.getAttendanceDate())
                .status(savedAttendance.getStatus().name())
                .markedAt(savedAttendance.getMarkedAt())
                .build();
    }
    
    @Override
    public List<AttendanceResponseDto> getAttendanceByEmployeeId(
            Long employeeId) {

        List<Attendance> attendances =
                attendanceRepository.findByEmployee_Id(employeeId);

        return attendances.stream() .map(attendance ->AttendanceResponseDto.builder()
.id(attendance.getId())
                                .employeeId(
                                        attendance.getEmployee().getId())
                                .employeeName(
                                        attendance.getEmployee().getFirstName()
                                                + " "
                                                + attendance.getEmployee().getLastName())
                                .attendanceDate(
                                        attendance.getAttendanceDate())
                                .status(
                                        attendance.getStatus().name())
                                .markedAt(
                                        attendance.getMarkedAt())
                                .build())
                .toList();
    }
    
    @Override
    public List<AttendanceResponseDto> getTodayAttendance() {

        List<Attendance> attendances =
                attendanceRepository.findByAttendanceDate(
                        LocalDate.now());

        return attendances.stream()
                .map(attendance ->
                        AttendanceResponseDto.builder()
                                .id(attendance.getId())
                                .employeeId(
                                        attendance.getEmployee().getId())
                                .employeeName(
                                        attendance.getEmployee().getFirstName()
                                                + " "
                                                + attendance.getEmployee().getLastName())
                                .attendanceDate(
                                        attendance.getAttendanceDate())
                                .status(
                                        attendance.getStatus().name())
                                .markedAt(
                                        attendance.getMarkedAt())
                                .build())
                .toList();
    }
    
    
    
    @Override
    public AttendanceSummaryDto getTodaySummary() {

        LocalDate today = LocalDate.now();

        long totalEmployees =
                employeeRepository.countByStatus(
                        EmployeeStatus.ACTIVE);

        long present =
                attendanceRepository.countByAttendanceDateAndStatus(
                        today,
                        AttendanceStatus.PRESENT);

        long halfDay =
                attendanceRepository.countByAttendanceDateAndStatus(
                        today,
                        AttendanceStatus.HALF_DAY);

        long leave =
                attendanceRepository.countByAttendanceDateAndStatus(
                        today,
                        AttendanceStatus.LEAVE);

        long absent =
                totalEmployees - present - halfDay - leave;

        return AttendanceSummaryDto.builder()
                .totalEmployees(totalEmployees)
                .present(present)
                .halfDay(halfDay)
                .leave(leave)
                .absent(absent)
                .build();
    }
    
    @Override
    public List<AttendanceResponseDto>
    getAttendanceByDate(
            LocalDate date) {

        return attendanceRepository
                .findByAttendanceDate(date)
                .stream()
                .map(attendance ->
                        AttendanceResponseDto.builder()
                                .id(
                                        attendance.getId())
                                .employeeId(
                                        attendance.getEmployee()
                                                .getId())
                                .employeeName(
                                        attendance.getEmployee()
                                                .getFirstName()
                                                + " "
                                                + attendance.getEmployee()
                                                .getLastName())
                                .attendanceDate(
                                        attendance.getAttendanceDate())
                                .status(
                                        attendance.getStatus()
                                                .name())
                                .markedAt(
                                        attendance.getMarkedAt())
                                .build())
                .toList();
    }
    
    @Override
    public MonthlyAttendanceReportDto
    getMonthlyAttendanceReport(
            Long employeeId,
            Integer month,
            Integer year) {

        Employee employee =
                employeeRepository.findById(
                        employeeId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found"));

        LocalDate startDate =
                LocalDate.of(year, month, 1);

        LocalDate endDate =
                startDate.withDayOfMonth(
                        startDate.lengthOfMonth());

        Long presentDays =
                attendanceRepository
                        .countByEmployee_IdAndStatusAndAttendanceDateBetween(
                                employeeId,
                                AttendanceStatus.PRESENT,
                                startDate,
                                endDate);

        Long leaveDays =
                attendanceRepository
                        .countByEmployee_IdAndStatusAndAttendanceDateBetween(
                                employeeId,
                                AttendanceStatus.LEAVE,
                                startDate,
                                endDate);

        Long halfDayDays =
                attendanceRepository
                        .countByEmployee_IdAndStatusAndAttendanceDateBetween(
                                employeeId,
                                AttendanceStatus.HALF_DAY,
                                startDate,
                                endDate);

        Long absentDays =
                attendanceRepository
                        .countByEmployee_IdAndStatusAndAttendanceDateBetween(
                                employeeId,
                                AttendanceStatus.ABSENT,
                                startDate,
                                endDate);

        Long weekOffDays =
                attendanceRepository
                        .countByEmployee_IdAndStatusAndAttendanceDateBetween(
                                employeeId,
                                AttendanceStatus.WEEK_OFF,
                                startDate,
                                endDate);

        Long holidayDays =
                attendanceRepository
                        .countByEmployee_IdAndStatusAndAttendanceDateBetween(
                                employeeId,
                                AttendanceStatus.HOLIDAY,
                                startDate,
                                endDate);

        return MonthlyAttendanceReportDto.builder()
                .employeeId(employee.getId())
                .employeeName(
                        employee.getFirstName()
                                + " "
                                + employee.getLastName())
                .presentDays(presentDays)
                .leaveDays(leaveDays)
                .halfDayDays(halfDayDays)
                .absentDays(absentDays)
                .weekOffDays(weekOffDays)
                .holidayDays(holidayDays)
                .build();
    }
    
    @Override
    public MonthlyAttendanceReportDto
    getMyMonthlyReport() {

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

        LocalDate today =
                LocalDate.now();

        return getMonthlyAttendanceReport(
                employee.getId(),
                today.getMonthValue(),
                today.getYear());
    }
}