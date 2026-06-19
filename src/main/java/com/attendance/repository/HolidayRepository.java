package com.attendance.repository;

import com.attendance.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HolidayRepository
        extends JpaRepository<Holiday, Long> {

    List<Holiday> findByHolidayDateGreaterThanEqualOrderByHolidayDateAsc(
            LocalDate date);
    
    boolean existsByHolidayDate(
            LocalDate holidayDate);
}