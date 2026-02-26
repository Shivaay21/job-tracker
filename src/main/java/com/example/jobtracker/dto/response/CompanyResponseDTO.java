package com.example.jobtracker.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompanyResponseDTO {

    private Long id;

    private String name;

    private String location;

    private String website;

    private LocalDateTime createdAt;
}
