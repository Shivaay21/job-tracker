package com.example.jobtracker.service;

import com.example.jobtracker.dto.PagedResponseDTO;
import com.example.jobtracker.dto.request.JobApplicationRequestDTO;
import com.example.jobtracker.dto.response.ApplicationStatisticsResponseDTO;
import com.example.jobtracker.dto.response.JobApplicationResponseDTO;
import com.example.jobtracker.entity.ApplicationStatus;


import java.time.LocalDateTime;
import java.util.List;

public interface JobApplicationService {
    JobApplicationResponseDTO createJobApplication(JobApplicationRequestDTO requestDTO);

    JobApplicationResponseDTO getApplicationById(Long id);

//    List<JobApplicationResponseDTO> getAllJobApplication();
    PagedResponseDTO<JobApplicationResponseDTO> getAllJobApplication(int page, int size, String sortBy, String sortDir);

    JobApplicationResponseDTO updateJobApplication(Long id, JobApplicationRequestDTO requestDTO);

    void deleteJobApplication(Long id);

    List<JobApplicationResponseDTO> getJobApplicationByStatus(ApplicationStatus status);

    List<JobApplicationResponseDTO> getJobApplicationByDateRange(LocalDateTime start, LocalDateTime end);

    long countByStatus(ApplicationStatus status);

    ApplicationStatisticsResponseDTO getApplicationStatistics();

}
