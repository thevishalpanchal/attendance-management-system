package com.attendance.service;

import java.util.List;

import com.attendance.dto.leave.LeaveApplyRequestDto;
import com.attendance.dto.leave.LeaveBalanceResponseDto;
import com.attendance.dto.leave.LeaveResponseDto;
import com.attendance.dto.leave.LeaveSummaryDto;

public interface LeaveService {

    LeaveResponseDto applyLeave(
            LeaveApplyRequestDto requestDto
    );
    
    List<LeaveResponseDto> getPendingLeaves();

    List<LeaveResponseDto> getLeavesByEmployeeId(
            Long employeeId);

    LeaveResponseDto getLeaveById(Long leaveId);

    void approveLeave(Long leaveId,Long approvedBy);

    void rejectLeave(Long leaveId);

    List<LeaveResponseDto> getApprovedLeaves();

    List<LeaveResponseDto> getRejectedLeaves();
    
    LeaveBalanceResponseDto getMyLeaveBalance();
    
    List<LeaveResponseDto> getMyLeaves();
    
    LeaveSummaryDto getLeaveSummary();
    
    LeaveBalanceResponseDto getLeaveBalanceByEmployeeId(Long employeeId);
    
    

}