package com.ams.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FacultyUpdateRequest(
    @NotBlank(message = "First name cannot be empty")
    String firstName,

    @NotBlank(message = "Last name cannot be empty")
    String lastName,

    String phone,

    @NotBlank(message = "Designation cannot be empty")
    String designation,

    @NotNull(message = "Department ID cannot be null")
    Long departmentId
) {}
