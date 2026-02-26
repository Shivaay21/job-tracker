package com.example.jobtracker.service;

import com.example.jobtracker.entity.ApplicationStatus;
import com.example.jobtracker.entity.JobApplication;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface JobApplicationService {
    JobApplication createJobApplication(JobApplication jobApplication);

    JobApplication getApplicationById(Long id);

    Page<JobApplication> getAllJobApplication(int page, int size, String sortBy, String sortDir);

    JobApplication updateJobApplication(Long id, JobApplication jobApplication);

    void deleteJobApplication(Long id);

    List<JobApplication> getJobApplicationByStatus(ApplicationStatus status);

    List<JobApplication> getJobApplicationByDateRange(LocalDateTime start, LocalDateTime end);

    long countByStatus(ApplicationStatus status);

}
