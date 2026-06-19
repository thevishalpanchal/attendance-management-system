package com.attendance.serviceimpl;

import com.attendance.dto.holiday.HolidayRequestDto;
import com.attendance.dto.holiday.HolidayResponseDto;
import com.attendance.entity.Holiday;
import com.attendance.exception.ResourceNotFoundException;
import com.attendance.repository.HolidayRepository;
import com.attendance.service.HolidayService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HolidayServiceImpl implements HolidayService {

    private final HolidayRepository holidayRepository;

    public HolidayServiceImpl(
            HolidayRepository holidayRepository) {

        this.holidayRepository = holidayRepository;
    }

    @Override
    public HolidayResponseDto createHoliday(
            HolidayRequestDto requestDto) {

        Holiday holiday = Holiday.builder()
                .holidayName(requestDto.getHolidayName())
                .holidayDate(requestDto.getHolidayDate())
                .description(requestDto.getDescription())
                .createdAt(LocalDateTime.now())
                .build();

        Holiday savedHoliday =
                holidayRepository.save(holiday);

        return mapToResponse(savedHoliday);
    }

    @Override
    public HolidayResponseDto getHolidayById(
            Long holidayId) {

        Holiday holiday =
                holidayRepository.findById(holidayId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Holiday not found"));

        return mapToResponse(holiday);
    }

    @Override
    public List<HolidayResponseDto> getAllHolidays() {

        return holidayRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<HolidayResponseDto> getUpcomingHolidays() {

        return holidayRepository
                .findByHolidayDateGreaterThanEqualOrderByHolidayDateAsc(
                        LocalDate.now())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public HolidayResponseDto updateHoliday(
            Long holidayId,
            HolidayRequestDto requestDto) {

        Holiday holiday =
                holidayRepository.findById(holidayId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Holiday not found"));

        holiday.setHolidayName(
                requestDto.getHolidayName());

        holiday.setHolidayDate(
                requestDto.getHolidayDate());

        holiday.setDescription(
                requestDto.getDescription());

        Holiday updatedHoliday =
                holidayRepository.save(holiday);

        return mapToResponse(updatedHoliday);
    }

    @Override
    public void deleteHoliday(
            Long holidayId) {

        Holiday holiday =
                holidayRepository.findById(holidayId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Holiday not found"));

        holidayRepository.delete(holiday);
    }

    private HolidayResponseDto mapToResponse(
            Holiday holiday) {

        return HolidayResponseDto.builder()
                .id(holiday.getId())
                .holidayName(holiday.getHolidayName())
                .holidayDate(holiday.getHolidayDate())
                .description(holiday.getDescription())
                .build();
    }
}