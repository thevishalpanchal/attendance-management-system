package com.attendance.service;

import java.time.LocalDate;
import java.util.List;

import com.attendance.dto.attendance.AttendanceRequestDto;
import com.attendance.dto.attendance.AttendanceResponseDto;
import com.attendance.dto.attendance.AttendanceSummaryDto;
import com.attendance.dto.attendance.MonthlyAttendanceReportDto;

public interface AttendanceService {

    AttendanceResponseDto markAttendance( AttendanceRequestDto requestDto);
    
    List<AttendanceResponseDto> getAttendanceByEmployeeId( Long employeeId );
    
    List<AttendanceResponseDto> getTodayAttendance();
    
    AttendanceSummaryDto getTodaySummary();
    
    List<AttendanceResponseDto>
    getAttendanceByDate(
            LocalDate date);
    
    MonthlyAttendanceReportDto
    getMonthlyAttendanceReport(
            Long employeeId,
            Integer month,
            Integer year
    );
    
    MonthlyAttendanceReportDto getMyMonthlyReport();

}