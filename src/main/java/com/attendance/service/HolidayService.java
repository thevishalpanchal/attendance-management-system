package com.attendance.service;

import com.attendance.dto.holiday.HolidayRequestDto;
import com.attendance.dto.holiday.HolidayResponseDto;

import java.util.List;

public interface HolidayService {

    HolidayResponseDto createHoliday(HolidayRequestDto requestDto);

    HolidayResponseDto getHolidayById(Long holidayId);

    List<HolidayResponseDto> getAllHolidays();

    List<HolidayResponseDto> getUpcomingHolidays();

    HolidayResponseDto updateHoliday(Long holidayId,HolidayRequestDto requestDto);

    void deleteHoliday(Long holidayId);
}