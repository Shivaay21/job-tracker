package com.example.jobtracker.service.impl;

import com.example.jobtracker.dto.request.CompanyRequestDTO;
import com.example.jobtracker.dto.response.CompanyResponseDTO;
import com.example.jobtracker.entity.Company;
import com.example.jobtracker.exception.ResourceNotFoundException;
import com.example.jobtracker.repository.CompanyRepository;
import com.example.jobtracker.service.CompanyService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, ModelMapper modelMapper){
        this.companyRepository = companyRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CompanyResponseDTO createCompany(CompanyRequestDTO requestDTO){
        Company company = modelMapper.map(requestDTO, Company.class);
        Company savedCompany = companyRepository.save(company);

        return modelMapper.map(savedCompany, CompanyResponseDTO.class);
    }

    @Override
    public List<CompanyResponseDTO> getAllCompanies(){
        List<Company> companies = companyRepository.findAll();
        return companies.stream().map(company ->
                        modelMapper.map(company, CompanyResponseDTO.class))
                        .toList();
    }

    @Override
    public CompanyResponseDTO getCompanyById(Long id){
        Company company = companyRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Company is not available for the given id "+id));
        return modelMapper.map(company, CompanyResponseDTO.class);
    }

    @Override
    public CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO requestDTO){
        Company existing = companyRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Company is not available for the given id "+id));
        existing.setName(requestDTO.getName());
        existing.setLocation(requestDTO.getLocation());
        existing.setWebsite(requestDTO.getWebsite());

        Company updated = companyRepository.save(existing);
        return modelMapper.map(updated, CompanyResponseDTO.class);
    }

    @Override
    public void deleteCompany(Long id){
        companyRepository.deleteById(id);
    }
}
