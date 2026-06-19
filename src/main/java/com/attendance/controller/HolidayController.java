package com.attendance.controller;

import com.attendance.dto.holiday.HolidayRequestDto;
import com.attendance.dto.holiday.HolidayResponseDto;
import com.attendance.service.HolidayService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/holidays")
public class HolidayController {

    private final HolidayService holidayService;

    public HolidayController(
            HolidayService holidayService) {

        this.holidayService = holidayService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<HolidayResponseDto>
    createHoliday(
            @RequestBody HolidayRequestDto requestDto) {

        return ResponseEntity.ok(
                holidayService.createHoliday(
                        requestDto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @GetMapping
    public ResponseEntity<List<HolidayResponseDto>>
    getAllHolidays() {

        return ResponseEntity.ok(
                holidayService.getAllHolidays());
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @GetMapping("/upcoming")
    public ResponseEntity<List<HolidayResponseDto>>
    getUpcomingHolidays() {

        return ResponseEntity.ok(
                holidayService.getUpcomingHolidays());
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @GetMapping("/{id}")
    public ResponseEntity<HolidayResponseDto>
    getHolidayById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                holidayService.getHolidayById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<HolidayResponseDto>
    updateHoliday(
            @PathVariable Long id,
            @RequestBody HolidayRequestDto requestDto) {

        return ResponseEntity.ok(
                holidayService.updateHoliday(
                        id,
                        requestDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    deleteHoliday(
            @PathVariable Long id) {

        holidayService.deleteHoliday(id);

        return ResponseEntity.ok(
                "Holiday deleted successfully");
    }
}