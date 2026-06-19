package com.attendance.dto.holiday;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HolidayRequestDto {

    private String holidayName;

    private LocalDate holidayDate;

    private String description;
}