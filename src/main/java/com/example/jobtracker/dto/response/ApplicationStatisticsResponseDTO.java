package com.example.jobtracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicationStatisticsResponseDTO {
    private long totalApplied;
    private long totalRejected;
    private long totalInterviews;
    private long totalSelected;
    private double successRate;
}
