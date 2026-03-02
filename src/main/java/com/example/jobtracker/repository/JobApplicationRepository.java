package com.example.jobtracker.repository;

import com.example.jobtracker.entity.ApplicationStatus;
import com.example.jobtracker.entity.JobApplication;
import com.example.jobtracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByStatus(ApplicationStatus status);

    List<JobApplication> findByAppliedDateBetween(LocalDateTime start, LocalDateTime end);

    long countByStatus(ApplicationStatus status);

    List<JobApplication> findByUser(User user);
}
