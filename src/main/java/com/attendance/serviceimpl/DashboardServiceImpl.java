package com.attendance.serviceimpl;

import com.attendance.dto.dashboard.DashboardSummaryDto;
import com.attendance.enums.AttendanceStatus;
import com.attendance.enums.EmployeeStatus;
import com.attendance.enums.LeaveRequestStatus;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.EmployeeRepository;
import com.attendance.repository.LeaveRequestRepository;
import com.attendance.service.DashboardService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DashboardServiceImpl
        implements DashboardService {

    private final EmployeeRepository employeeRepository;

    private final AttendanceRepository attendanceRepository;

    private final LeaveRequestRepository leaveRequestRepository;

    public DashboardServiceImpl(
            EmployeeRepository employeeRepository,
            AttendanceRepository attendanceRepository,
            LeaveRequestRepository leaveRequestRepository) {

        this.employeeRepository =
                employeeRepository;

        this.attendanceRepository =
                attendanceRepository;

        this.leaveRequestRepository =
                leaveRequestRepository;
    }

    @Override
    public DashboardSummaryDto
    getDashboardSummary() {

        LocalDate today = LocalDate.now();

        return DashboardSummaryDto.builder()

                .totalEmployees(
                        employeeRepository.count())

                .activeEmployees(
                        employeeRepository.countByStatus(
                                EmployeeStatus.ACTIVE))

                .presentToday(
                        attendanceRepository
                                .countByAttendanceDateAndStatus(
                                        today,
                                        AttendanceStatus.PRESENT))

                .leaveToday(
                        attendanceRepository
                                .countByAttendanceDateAndStatus(
                                        today,
                                        AttendanceStatus.LEAVE))

                .halfDayToday(
                        attendanceRepository
                                .countByAttendanceDateAndStatus(
                                        today,
                                        AttendanceStatus.HALF_DAY))

                .pendingLeaves(
                        leaveRequestRepository
                                .countByStatus(
                                        LeaveRequestStatus.PENDING))

                .build();
    }
}