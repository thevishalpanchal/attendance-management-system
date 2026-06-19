package com.attendance.controller;

import com.attendance.dto.attendance.AttendanceRequestDto;
import com.attendance.dto.attendance.AttendanceResponseDto;
import com.attendance.dto.attendance.AttendanceSummaryDto;
import com.attendance.dto.attendance.MonthlyAttendanceReportDto;
import com.attendance.service.AttendanceService;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(
            AttendanceService attendanceService) {

        this.attendanceService = attendanceService;
    }

    @PostMapping("/mark")
    public ResponseEntity<AttendanceResponseDto> markAttendance(
            @RequestBody AttendanceRequestDto requestDto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(attendanceService.markAttendance(requestDto));
    }
    
    @GetMapping("/employee/{employeeId}")
   public ResponseEntity<List<AttendanceResponseDto>>
    getAttendanceByEmployeeId(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                attendanceService
                        .getAttendanceByEmployeeId(employeeId));
    }
    
    @GetMapping("/today")
    public ResponseEntity<List<AttendanceResponseDto>>
    getTodayAttendance() {

        return ResponseEntity.ok(
                attendanceService.getTodayAttendance()
        );
    }
    
    @GetMapping("/today-summary")
    public ResponseEntity<AttendanceSummaryDto> getTodaySummary() {

        return ResponseEntity.ok(
                attendanceService.getTodaySummary()
        );
    }
    
    
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @GetMapping("/date/{date}")
    public ResponseEntity<List<AttendanceResponseDto>>
    getAttendanceByDate(
            @PathVariable LocalDate date) {

        return ResponseEntity.ok(
                attendanceService
                        .getAttendanceByDate(date));
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @GetMapping("/monthly-report")
    public ResponseEntity<MonthlyAttendanceReportDto>
    getMonthlyAttendanceReport(

            @RequestParam Long employeeId,

            @RequestParam Integer month,

            @RequestParam Integer year) {

        return ResponseEntity.ok(
                attendanceService
                        .getMonthlyAttendanceReport(
                                employeeId,
                                month,
                                year));
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','HR','EMPLOYEE')")
    @GetMapping("/my-monthly-report")
    public ResponseEntity<MonthlyAttendanceReportDto>
    getMyMonthlyReport() {

        return ResponseEntity.ok(
                attendanceService
                        .getMyMonthlyReport());
    }
}