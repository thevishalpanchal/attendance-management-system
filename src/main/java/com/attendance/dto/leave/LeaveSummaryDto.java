package com.attendance.dto.leave;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveSummaryDto {

    private Long pendingLeaves;

    private Long approvedLeaves;

    private Long rejectedLeaves;
}