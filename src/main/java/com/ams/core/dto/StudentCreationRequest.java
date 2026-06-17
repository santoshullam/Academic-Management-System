package com.ams.core.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record StudentCreationRequest(
    @NotBlank(message = "Username cannot be empty")
    String username,

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email format is invalid")
    String email,

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    String password,

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
