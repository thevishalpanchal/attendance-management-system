package com.attendance.dto.holiday;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HolidayResponseDto {

    private Long id;

    private String holidayName;

    private LocalDate holidayDate;

    private String description;
}