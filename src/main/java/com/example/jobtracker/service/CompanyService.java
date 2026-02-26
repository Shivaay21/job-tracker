package com.example.jobtracker.service;

import com.example.jobtracker.dto.request.CompanyRequestDTO;
import com.example.jobtracker.dto.response.CompanyResponseDTO;
import com.example.jobtracker.entity.Company;

import java.util.List;

public interface CompanyService {
    CompanyResponseDTO createCompany(CompanyRequestDTO requestDTO);

    CompanyResponseDTO getCompanyById(Long id);

    List<CompanyResponseDTO> getAllCompanies();

    CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO requestDTO);

    void deleteCompany(Long id);
}
