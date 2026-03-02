package com.example.jobtracker.controller;

import com.example.jobtracker.dto.PagedResponseDTO;
import com.example.jobtracker.dto.request.JobApplicationRequestDTO;
import com.example.jobtracker.dto.response.ApplicationStatisticsResponseDTO;
import com.example.jobtracker.dto.response.JobApplicationResponseDTO;
import com.example.jobtracker.entity.ApplicationStatus;
import com.example.jobtracker.entity.User;
import com.example.jobtracker.repository.UserRepository;
import com.example.jobtracker.service.JobApplicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Job Application APIs", description = "Operations related to job application tracking")
@RestController
@RequestMapping("/api/job-applications")
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;
    private final UserRepository userRepository;

    public JobApplicationController(JobApplicationService jobApplicationService, UserRepository userRepository){
        this.jobApplicationService = jobApplicationService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<JobApplicationResponseDTO> createJobApplication(@Valid @RequestBody JobApplicationRequestDTO requestDTO){
        JobApplicationResponseDTO saved = jobApplicationService.createJobApplication(requestDTO);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PagedResponseDTO<JobApplicationResponseDTO>> getAllJobApplication(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "roleTitle") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir){

        PagedResponseDTO<JobApplicationResponseDTO> jobPage = jobApplicationService.getAllJobApplication(page, size, sortBy, sortDir);
        return ResponseEntity.ok(jobPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationResponseDTO> getApplicationById(@PathVariable Long id){
        return ResponseEntity.ok(jobApplicationService.getApplicationById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobApplicationResponseDTO> updateJobApplication(@PathVariable Long id, @Valid @RequestBody JobApplicationRequestDTO requestDTO){
        return ResponseEntity.ok(jobApplicationService.updateJobApplication(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobApplication(@PathVariable Long id){
                jobApplicationService.deleteJobApplication(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<JobApplicationResponseDTO>> getJobApplicationByStatus(@PathVariable ApplicationStatus status){
        List<JobApplicationResponseDTO> jobs = jobApplicationService.getJobApplicationByStatus(status);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/applied")
    public ResponseEntity<List<JobApplicationResponseDTO>> getJobApplicationByDateRange(
            @RequestParam("start")String start,
            @RequestParam("end")String end){
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);

        List<JobApplicationResponseDTO> jobs = jobApplicationService.getJobApplicationByDateRange(startDate,endDate);
        return ResponseEntity.ok(jobs);

    }

    @GetMapping("/statistics/{status}")
    public ResponseEntity<Long> countByStatus(@PathVariable ApplicationStatus status){
        long count = jobApplicationService.countByStatus(status);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/statistics/summary")
    public ResponseEntity<ApplicationStatisticsResponseDTO> getApplicationStatistics() {
        ApplicationStatisticsResponseDTO stats = jobApplicationService.getApplicationStatistics();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/my-applications")
    public ResponseEntity<List<JobApplicationResponseDTO>> getMyApplications(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<JobApplicationResponseDTO> applications = jobApplicationService.getJobApplicationByUser(currentUser);
        return ResponseEntity.ok(applications);
    }
}
