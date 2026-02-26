package com.example.jobtracker.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Valid
public class CompanyRequestDTO {


    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be 2-100 chars")
    private String name;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Website is required")
    private String website;

}
