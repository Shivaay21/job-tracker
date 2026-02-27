package com.example.jobtracker.controller;

import com.example.jobtracker.dto.request.JobApplicationRequestDTO;
import com.example.jobtracker.dto.response.JobApplicationResponseDTO;
import com.example.jobtracker.entity.ApplicationStatus;
import com.example.jobtracker.service.JobApplicationService;
import com.example.jobtracker.service.impl.JobApplicationServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/job-applications")
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService){
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping
    public ResponseEntity<JobApplicationResponseDTO> createJobApplication(@Valid @RequestBody JobApplicationRequestDTO requestDTO){
        JobApplicationResponseDTO saved = jobApplicationService.createJobApplication(requestDTO);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<JobApplicationResponseDTO>> getAllJobApplication(){
        List<JobApplicationResponseDTO> jobs = jobApplicationService.getAllJobApplication();
        return ResponseEntity.ok(jobs);
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
}
