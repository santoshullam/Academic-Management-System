package com.ams.core.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FacultyCreationRequest(
    @NotBlank(message = "Username cannot be empty")
    String username,

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email format is invalid")
    String email,

    @NotBlank(message = "Password cannot be empty")
    String password,

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
