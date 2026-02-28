package com.example.jobtracker.service.impl;

import com.example.jobtracker.dto.PagedResponseDTO;
import com.example.jobtracker.dto.request.CompanyRequestDTO;
import com.example.jobtracker.dto.response.CompanyResponseDTO;
import com.example.jobtracker.entity.Company;
import com.example.jobtracker.exception.DuplicateResourceException;
import com.example.jobtracker.exception.ResourceNotFoundException;
import com.example.jobtracker.repository.CompanyRepository;
import com.example.jobtracker.service.CompanyService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        if(companyRepository.existsByName(requestDTO.getName())){
            throw new DuplicateResourceException("Company already exists with name " + requestDTO.getName());
        }
        Company company = modelMapper.map(requestDTO, Company.class);
        Company savedCompany = companyRepository.save(company);

        return modelMapper.map(savedCompany, CompanyResponseDTO.class);
    }

    @Override
    public PagedResponseDTO<CompanyResponseDTO> getAllCompanies(int page, int size, String sortBy, String sortDir){
        Sort sort = sortDir.equalsIgnoreCase("desc")?
                Sort.by(sortBy).descending():
                Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Company> companyPage = companyRepository.findAll(pageable);
        List<CompanyResponseDTO> content = companyPage.stream().map(company ->
                modelMapper.map(company, CompanyResponseDTO.class)).collect(Collectors.toList());

        return new PagedResponseDTO<>(
                content,
                companyPage.getNumber(),
                companyPage.getSize(),
                companyPage.getTotalElements(),
                companyPage.getTotalPages(),
                companyPage.isLast(),
                companyPage.hasNext(),
                companyPage.hasPrevious()
                );
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
        Company company = companyRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Company not found with id"+ id));
        companyRepository.delete(company);
    }

    @Override
    public PagedResponseDTO<CompanyResponseDTO> searchCompaniesByName(
            String name,
            int page,
            int size,
            String sortBy,
            String sortDir){
        Sort sort = sortBy.equalsIgnoreCase("desc")?
                Sort.by(sortBy).descending():
                Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Company> companyPage = companyRepository.findByNameContainingIgnoreCase(name,pageable);
        List<CompanyResponseDTO> content = companyPage.
                stream().map(company ->
                        modelMapper.map(company, CompanyResponseDTO.class))
                .collect(Collectors.toList());

        return new PagedResponseDTO<>(
                content,
                companyPage.getNumber(),
                companyPage.getSize(),
                companyPage.getTotalElements(),
                companyPage.getTotalPages(),
                companyPage.isLast(),
                companyPage.hasNext(),
                companyPage.hasPrevious()
        );
    }
}
