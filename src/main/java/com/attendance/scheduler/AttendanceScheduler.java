package com.attendance.scheduler;

import com.attendance.entity.Attendance;
import com.attendance.entity.Employee;
import com.attendance.entity.LeaveRequest;
import com.attendance.enums.AttendanceStatus;
import com.attendance.enums.EmployeeStatus;
import com.attendance.enums.LeaveDuration;
import com.attendance.enums.LeaveRequestStatus;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.EmployeeRepository;
import com.attendance.repository.HolidayRepository;
import com.attendance.repository.LeaveRequestRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class AttendanceScheduler {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final HolidayRepository holidayRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    public AttendanceScheduler(
            EmployeeRepository employeeRepository,
            AttendanceRepository attendanceRepository,
            HolidayRepository holidayRepository,
            LeaveRequestRepository leaveRequestRepository) {

        this.employeeRepository = employeeRepository;
        this.attendanceRepository = attendanceRepository;
        this.holidayRepository = holidayRepository;
        this.leaveRequestRepository = leaveRequestRepository;
    }

   @Scheduled(cron = "0 0 21 * * *")
//    @Scheduled(cron = "0 */1 * * * *")
    public void markAttendanceStatus() {
    	 System.out.println("Scheduler Running At : "
    	            + LocalDateTime.now());
        LocalDate today = LocalDate.now();

        List<Employee> employees =
                employeeRepository.findByStatus(
                        EmployeeStatus.ACTIVE);

        for (Employee employee : employees) {

            boolean attendanceExists =
                    attendanceRepository
                            .findByEmployee_IdAndAttendanceDate(
                                    employee.getId(),
                                    today)
                            .isPresent();

            if (attendanceExists) {
                continue;
            }

            /*
             * SATURDAY / SUNDAY
             */
            if (today.getDayOfWeek() == DayOfWeek.SATURDAY
                    || today.getDayOfWeek() == DayOfWeek.SUNDAY) {

                createAttendance(
                        employee,
                        AttendanceStatus.WEEK_OFF);

                continue;
            }

            /*
             * HOLIDAY
             */
            if (holidayRepository.existsByHolidayDate(
                    today)) {

                createAttendance(
                        employee,
                        AttendanceStatus.HOLIDAY);

                continue;
            }

            /*
             * APPROVED LEAVE
             */
            Optional<LeaveRequest> leaveRequest =
                    leaveRequestRepository
                            .findByEmployee_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndStatus(
                                    employee.getId(),
                                    today,
                                    today,
                                    LeaveRequestStatus.APPROVED
                            );

            if (leaveRequest.isPresent()) {

                LeaveRequest leave =
                        leaveRequest.get();

                if (leave.getLeaveDuration()
                        == LeaveDuration.HALF_DAY) {

                    createAttendance(
                            employee,
                            AttendanceStatus.HALF_DAY);

                } else {

                    createAttendance(
                            employee,
                            AttendanceStatus.LEAVE);
                }

                continue;
            }

            /*
             * ABSENT
             */
            createAttendance(
                    employee,
                    AttendanceStatus.ABSENT);
        }

        System.out.println(
                "Attendance scheduler executed successfully");
    }

    private void createAttendance(
            Employee employee,
            AttendanceStatus status) {

        Attendance attendance =
                Attendance.builder()
                        .employee(employee)
                        .attendanceDate(
                                LocalDate.now())
                        .status(status)
                        .markedAt(
                                LocalDateTime.now())
                        .createdAt(
                                LocalDateTime.now())
                        .build();

        attendanceRepository.save(
                attendance);
    }
}