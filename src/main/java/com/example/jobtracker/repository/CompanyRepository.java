package com.example.jobtracker.repository;

import com.example.jobtracker.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByName(String name);
    Page<Company> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
