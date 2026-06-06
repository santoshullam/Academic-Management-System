package com.ams.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

public record StudentUpdateRequest(
    @NotBlank(message = "First name cannot be empty")
    String firstName,

    @NotBlank(message = "Last name cannot be empty")
    String lastName,

    String phone,

    @Past(message = "Date of birth must be in the past")
    LocalDate dateOfBirth,

    @NotNull(message = "Department ID cannot be null")
    Long departmentId
) {}
