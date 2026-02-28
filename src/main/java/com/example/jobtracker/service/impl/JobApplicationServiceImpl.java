package com.example.jobtracker.service.impl;

import com.example.jobtracker.dto.PagedResponseDTO;
import com.example.jobtracker.dto.request.JobApplicationRequestDTO;
import com.example.jobtracker.dto.response.ApplicationStatisticsResponseDTO;
import com.example.jobtracker.dto.response.JobApplicationResponseDTO;
import com.example.jobtracker.entity.ApplicationStatus;
import com.example.jobtracker.entity.Company;
import com.example.jobtracker.entity.JobApplication;
import com.example.jobtracker.exception.ResourceNotFoundException;
import com.example.jobtracker.repository.CompanyRepository;
import com.example.jobtracker.repository.JobApplicationRepository;
import com.example.jobtracker.service.JobApplicationService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobApplicationServiceImpl implements JobApplicationService {
    private final JobApplicationRepository jobApplicationRepository;
    private final ModelMapper modelMapper;
    private CompanyRepository companyRepository;

    public JobApplicationServiceImpl(JobApplicationRepository jobApplicationRepository, ModelMapper modelMapper, CompanyRepository companyRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.modelMapper = modelMapper;
        this.companyRepository = companyRepository;
    }

//    Isko smjhna hoga acche se tab aage move karenge
    @Override
    public JobApplicationResponseDTO createJobApplication(JobApplicationRequestDTO requestDTO) {
        Company company = companyRepository.findById(requestDTO.getCompanyId()).orElseThrow(()->
                new ResourceNotFoundException("Company not found with id"+requestDTO.getCompanyId()));

        JobApplication jobApplication = new JobApplication();
        jobApplication.setRoleTitle(requestDTO.getRoleTitle());
        jobApplication.setStatus(requestDTO.getStatus());
        jobApplication.setAppliedDate(requestDTO.getAppliedDate());
        jobApplication.setInterviewDate(requestDTO.getInterviewDate());
        jobApplication.setNotes(requestDTO.getNotes());
        jobApplication.setCompany(company);
        JobApplication savedJob = jobApplicationRepository.save(jobApplication);

        JobApplicationResponseDTO response = modelMapper.map(savedJob, JobApplicationResponseDTO.class);
        response.setCompanyId(company.getId());
        response.setCompanyName(company.getName());

        return response;
    }

    @Override
    public JobApplicationResponseDTO getApplicationById(Long id) {
        JobApplication job = jobApplicationRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Job not found with the given id " + id));
        return modelMapper.map(job, JobApplicationResponseDTO.class);
    }


    @Override
    public PagedResponseDTO<JobApplicationResponseDTO> getAllJobApplication(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortBy.equalsIgnoreCase("desc")?
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
        JobApplication existingJob = jobApplicationRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Job not found with the given id " + id));
        existingJob.setRoleTitle(requestDTO.getRoleTitle());
        existingJob.setStatus(requestDTO.getStatus());
        existingJob.setAppliedDate(requestDTO.getAppliedDate());
        existingJob.setInterviewDate(requestDTO.getInterviewDate());
        existingJob.setNotes(requestDTO.getNotes());

        JobApplication updated = jobApplicationRepository.save(existingJob);
        return modelMapper.map(updated, JobApplicationResponseDTO.class);
    }

    @Override
    public void deleteJobApplication(Long id) {
        JobApplication job = jobApplicationRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Job Application not found with the given id"+id));
        jobApplicationRepository.delete(job);
    }

    @Override
    public List<JobApplicationResponseDTO> getJobApplicationByStatus(ApplicationStatus status) {
        List<JobApplication> jobs = jobApplicationRepository.findByStatus(status);

        return jobs.stream()
                .map(jobApplication ->
                        modelMapper.map(jobApplication, JobApplicationResponseDTO.class))
                        .toList();
    }

    @Override
    public List<JobApplicationResponseDTO> getJobApplicationByDateRange(LocalDateTime start, LocalDateTime end) {
        List<JobApplication> jobs = jobApplicationRepository
                .findByAppliedDateBetween(start, end);

        return jobs.stream().map(jobApplication ->
                        modelMapper.map(jobApplication, JobApplicationResponseDTO.class))
                        .toList();
    }

    @Override
    public long countByStatus(ApplicationStatus status) {
        return jobApplicationRepository.countByStatus(status);
    }

    @Override
    public ApplicationStatisticsResponseDTO getApplicationStatistics(){
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
}
