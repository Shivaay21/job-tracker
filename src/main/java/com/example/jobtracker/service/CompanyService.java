package com.example.jobtracker.service;

import com.example.jobtracker.dto.PagedResponseDTO;
import com.example.jobtracker.dto.request.CompanyRequestDTO;
import com.example.jobtracker.dto.response.CompanyResponseDTO;
import java.util.List;


public interface CompanyService {
    CompanyResponseDTO createCompany(CompanyRequestDTO requestDTO);

    CompanyResponseDTO getCompanyById(Long id);

    PagedResponseDTO<CompanyResponseDTO> getAllCompanies(int page, int size, String sortBy, String sortDir);

    CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO requestDTO);

    void deleteCompany(Long id);

    PagedResponseDTO<CompanyResponseDTO> searchCompaniesByName(String name, int page, int size, String sortBy, String sortDir);
}
