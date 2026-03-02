package com.example.jobtracker.service.impl;

import com.example.jobtracker.dto.PagedResponseDTO;
import com.example.jobtracker.dto.request.JobApplicationRequestDTO;
import com.example.jobtracker.dto.response.ApplicationStatisticsResponseDTO;
import com.example.jobtracker.dto.response.JobApplicationResponseDTO;
import com.example.jobtracker.entity.*;
import com.example.jobtracker.exception.ResourceNotFoundException;
import com.example.jobtracker.repository.CompanyRepository;
import com.example.jobtracker.repository.JobApplicationRepository;
import com.example.jobtracker.repository.UserRepository;
import com.example.jobtracker.service.JobApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class JobApplicationServiceImpl implements JobApplicationService {
    private final JobApplicationRepository jobApplicationRepository;
    private final ModelMapper modelMapper;
    private CompanyRepository companyRepository;
    private UserRepository userRepository;

    public JobApplicationServiceImpl(JobApplicationRepository jobApplicationRepository, ModelMapper modelMapper, CompanyRepository companyRepository, UserRepository userRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.modelMapper = modelMapper;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

//    Isko smjhna hoga acche se tab aage move karenge
    @Override
    public JobApplicationResponseDTO createJobApplication(JobApplicationRequestDTO requestDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        log.info("Creating job application for user: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->{
                    log.warn("User not found with email: {}", email);
                    return new ResourceNotFoundException("User not found");
                });

        Company company = companyRepository.findById(requestDTO.getCompanyId()).orElseThrow(()->{
            log.warn("Company not found with id {}",requestDTO.getCompanyId());
            return new ResourceNotFoundException("Company not found with id"+requestDTO.getCompanyId());
        });

        JobApplication jobApplication = new JobApplication();
        jobApplication.setRoleTitle(requestDTO.getRoleTitle());
        jobApplication.setStatus(requestDTO.getStatus());
        jobApplication.setAppliedDate(requestDTO.getAppliedDate());
        jobApplication.setInterviewDate(requestDTO.getInterviewDate());
        jobApplication.setNotes(requestDTO.getNotes());
        jobApplication.setCompany(company);
        jobApplication.setUser(user);

        JobApplication savedJob = jobApplicationRepository.save(jobApplication);

        log.info("Job application created successfully with id: {}", savedJob.getId());

        JobApplicationResponseDTO response = modelMapper.map(savedJob, JobApplicationResponseDTO.class);
        response.setCompanyId(company.getId());
        response.setCompanyName(company.getName());

        return response;
    }

    @Override
    public JobApplicationResponseDTO getApplicationById(Long id) {
        JobApplication job = jobApplicationRepository.findById(id).orElseThrow(() -> {
            log.warn("Job application not found with id {}", id);
            return new ResourceNotFoundException("Job not found with the given id " + id);
        });
        return modelMapper.map(job, JobApplicationResponseDTO.class);
    }


    @Override
    public PagedResponseDTO<JobApplicationResponseDTO> getAllJobApplication(int page, int size, String sortBy, String sortDir) {
        log.debug("Fetching job applications - page: {}, size: {}", page, size);

        Sort sort = sortDir.equalsIgnoreCase("desc")?
                Sort.by(sortBy).descending():
                Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<JobApplication> jobPage = jobApplicationRepository.findAll(pageable);
        List<JobApplicationResponseDTO> content = jobPage.stream().map(jobApplication ->
                modelMapper.map(jobApplication, JobApplicationResponseDTO.class)).collect(Collectors.toList());

        return new PagedResponseDTO<>(
                content,
                jobPage.getNumber(),
                jobPage.getSize(),
                jobPage.getTotalElements(),
                jobPage.getTotalPages(),
                jobPage.isLast(),
                jobPage.hasNext(),
                jobPage.hasPrevious()
        );
    }

    @Override
    public JobApplicationResponseDTO updateJobApplication(Long id, JobApplicationRequestDTO requestDTO) {

        log.info("Updating job application with id: {}", id);

        JobApplication existingJob = jobApplicationRepository.findById(id).orElseThrow(() -> {
            log.warn("Job application not found with id {}", id);
            return new ResourceNotFoundException("Job not found with the given id " + id);
        });
        existingJob.setRoleTitle(requestDTO.getRoleTitle());
        existingJob.setStatus(requestDTO.getStatus());
        existingJob.setAppliedDate(requestDTO.getAppliedDate());
        existingJob.setInterviewDate(requestDTO.getInterviewDate());
        existingJob.setNotes(requestDTO.getNotes());

        JobApplication updated = jobApplicationRepository.save(existingJob);
        log.info("Job application updated successfully with id {}", id);
        return modelMapper.map(updated, JobApplicationResponseDTO.class);
    }

    @Override
    public void deleteJobApplication(Long id) {
        JobApplication job = jobApplicationRepository.findById(id).orElseThrow(()-> {
           log.warn("Job application not found with id {}", id);
           return new ResourceNotFoundException("Job Application not found with the given id"+id);
        });
        jobApplicationRepository.delete(job);
        log.info("Job application deleted successfully with id {}",id);
    }

    @Override
    public List<JobApplicationResponseDTO> getJobApplicationByStatus(ApplicationStatus status) {

        log.debug("Fetching job applications by status: {}", status);
        List<JobApplication> jobs = jobApplicationRepository.findByStatus(status);

        return jobs.stream()
                .map(jobApplication ->
                        modelMapper.map(jobApplication, JobApplicationResponseDTO.class))
                        .toList();
    }

    @Override
    public List<JobApplicationResponseDTO> getJobApplicationByDateRange(LocalDateTime start, LocalDateTime end) {
        log.debug("Fetching job applications between {} and {}", start, end);
        List<JobApplication> jobs = jobApplicationRepository
                .findByAppliedDateBetween(start, end);

        return jobs.stream().map(jobApplication ->
                        modelMapper.map(jobApplication, JobApplicationResponseDTO.class))
                        .toList();
    }

    @Override
    public long countByStatus(ApplicationStatus status) {
        log.debug("Counting job application by status {}", status);
        return jobApplicationRepository.countByStatus(status);
    }

    @Override
    public ApplicationStatisticsResponseDTO getApplicationStatistics(){
        log.info("Generating job application statistics");

        long totalApplied = jobApplicationRepository.count();
        long totalRejected = jobApplicationRepository.countByStatus(ApplicationStatus.REJECTED);
        long totalInterviews = jobApplicationRepository.countByStatus(ApplicationStatus.INTERVIEW);
        long totalSelected = jobApplicationRepository.countByStatus(ApplicationStatus.SELECTED);

        double successRate = totalApplied > 0 ? (totalSelected * 100.0) / totalApplied : 0.0;

        return new ApplicationStatisticsResponseDTO(
                totalApplied,
                totalRejected,
                totalInterviews,
                totalSelected,
                successRate
        );

    }
// Isko bhi dekhna padega 28-02-2026
    public List<JobApplicationResponseDTO> getJobApplicationByUser(User user){

        log.debug("Fetching job application for user id {}", user.getId());
        List<JobApplication> jobs;

        if(user.getRole() == Role.ADMIN){
            jobs = jobApplicationRepository.findAll();
        }
        else {
            jobs = jobApplicationRepository.findByUser(user);
        }

        return jobs.stream()
                .map(job ->{
                    JobApplicationResponseDTO dto = modelMapper.map(job, JobApplicationResponseDTO.class);
                    dto.setCompanyId(job.getCompany().getId());
                    dto.setCompanyName(job.getCompany().getName());
                    return dto;
                }).toList();
    }
}
