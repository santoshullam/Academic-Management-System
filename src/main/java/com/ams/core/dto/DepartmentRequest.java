package com.ams.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DepartmentRequest(
    @NotBlank(message = "Department name cannot be empty")
    String name,

    @NotBlank(message = "Department code cannot be empty")
    @Size(min = 2, max = 10, message = "Department code must be between 2 and 10 characters")
    String code
) {}
