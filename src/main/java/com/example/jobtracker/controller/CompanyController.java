package com.example.jobtracker.controller;

import com.example.jobtracker.dto.request.CompanyRequestDTO;
import com.example.jobtracker.dto.response.CompanyResponseDTO;
import com.example.jobtracker.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService){
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<CompanyResponseDTO> createCompany(@Valid @RequestBody CompanyRequestDTO requestDTO){
        CompanyResponseDTO saved = companyService.createCompany(requestDTO);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponseDTO>> getAllCompanies(){
        List<CompanyResponseDTO> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> getCompanyById(@PathVariable Long id){
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> updateCompany(@PathVariable Long id,@Valid @RequestBody CompanyRequestDTO requestDTO){
        return ResponseEntity.ok(companyService.updateCompany(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id){
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
