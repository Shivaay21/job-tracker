package com.example.jobtracker.dto.response;

import com.example.jobtracker.entity.ApplicationStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobApplicationResponseDTO {
    private Long id;

    private String roleTitle;

    private ApplicationStatus status;

    private LocalDateTime appliedDate;

    private LocalDateTime interviewDate;

    private String notes;
}
