package com.example.jobtracker.controller;

import com.example.jobtracker.dto.PagedResponseDTO;
import com.example.jobtracker.dto.request.CompanyRequestDTO;
import com.example.jobtracker.dto.response.CompanyResponseDTO;
import com.example.jobtracker.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Company APIs", description = "Operations related to company management")
@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService){
        this.companyService = companyService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CompanyResponseDTO> createCompany(@Valid @RequestBody CompanyRequestDTO requestDTO){
        CompanyResponseDTO saved = companyService.createCompany(requestDTO);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all companies",
            description = "Fetches paginated and sorted list of companies"
    )
    @GetMapping
    public ResponseEntity<PagedResponseDTO<CompanyResponseDTO>> getAllCompanies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "roleTitle") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ){
        PagedResponseDTO<CompanyResponseDTO> companyPage = companyService.getAllCompanies(page, size, sortBy, sortDir);
        return ResponseEntity.ok(companyPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> getCompanyById(@PathVariable Long id){
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<PagedResponseDTO<CompanyResponseDTO>> searchCompanies(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ){
        PagedResponseDTO<CompanyResponseDTO> companies = companyService.searchCompaniesByName(name, page,size,sortBy,sortDir);
        return ResponseEntity.ok(companies);
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
