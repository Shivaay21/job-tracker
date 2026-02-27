package com.example.jobtracker.dto.request;

import com.example.jobtracker.entity.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobApplicationRequestDTO {

    @NotBlank(message = "Role Title is required")
    private String roleTitle;

    private ApplicationStatus status;

    @NotNull(message = "Applied Date is required")
    private LocalDateTime appliedDate;

    @NotNull(message = "Interview Date is required")
    private LocalDateTime interviewDate;

    @NotBlank(message = "Notes is required")
    private String notes;

    @NotNull(message = "Company ID is required")
    private Long companyId;
}
