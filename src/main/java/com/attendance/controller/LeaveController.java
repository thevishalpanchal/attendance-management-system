package com.attendance.controller;

import com.attendance.dto.leave.LeaveApplyRequestDto;
import com.attendance.dto.leave.LeaveBalanceResponseDto;
import com.attendance.dto.leave.LeaveResponseDto;
import com.attendance.dto.leave.LeaveSummaryDto;
import com.attendance.service.LeaveService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(
            LeaveService leaveService) {

        this.leaveService = leaveService;
    }

    @PostMapping("/apply")
    public ResponseEntity<LeaveResponseDto> applyLeave(
            @RequestBody LeaveApplyRequestDto requestDto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        leaveService.applyLeave(requestDto)
                );
    }
    
    @GetMapping("/pending")
    public ResponseEntity<List<LeaveResponseDto>>
    getPendingLeaves() {

        return ResponseEntity.ok(
                leaveService.getPendingLeaves());
    }
    
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveResponseDto>>
    getLeavesByEmployeeId(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                leaveService.getLeavesByEmployeeId(
                        employeeId));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<LeaveResponseDto>
    getLeaveById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                leaveService.getLeaveById(id));
    }
    
    @PatchMapping("/{id}/approve")
    public ResponseEntity<String>
    approveLeave(
            @PathVariable Long id,
            @RequestParam Long approvedBy) {

        leaveService.approveLeave(
                id,
                approvedBy);

        return ResponseEntity.ok(
                "Leave approved successfully");
    }
    
    @PatchMapping("/{id}/reject")
    public ResponseEntity<String>
    rejectLeave(
            @PathVariable Long id) {

        leaveService.rejectLeave(id);

        return ResponseEntity.ok(
                "Leave rejected successfully");
    }
    
    @GetMapping("/approved")
    public ResponseEntity<List<LeaveResponseDto>>
    getApprovedLeaves() {

        return ResponseEntity.ok(
                leaveService.getApprovedLeaves());
    }
    
    @GetMapping("/rejected")
    public ResponseEntity<List<LeaveResponseDto>>
    getRejectedLeaves() {

        return ResponseEntity.ok(
                leaveService.getRejectedLeaves());
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','HR','EMPLOYEE')")
    @GetMapping("/my-balance")
    public ResponseEntity<LeaveBalanceResponseDto>
    getMyLeaveBalance() {

        return ResponseEntity.ok(
                leaveService.getMyLeaveBalance());
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','HR','EMPLOYEE')")
    @GetMapping("/my-leaves")
    public ResponseEntity<List<LeaveResponseDto>>
    getMyLeaves() {

        return ResponseEntity.ok(
                leaveService.getMyLeaves());
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @GetMapping("/summary")
    public ResponseEntity<LeaveSummaryDto>
    getLeaveSummary() {

        return ResponseEntity.ok(
                leaveService.getLeaveSummary());
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @GetMapping("/balance/{employeeId}")
    public ResponseEntity<LeaveBalanceResponseDto>
    getLeaveBalanceByEmployeeId(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                leaveService
                        .getLeaveBalanceByEmployeeId(
                                employeeId));
    }
}