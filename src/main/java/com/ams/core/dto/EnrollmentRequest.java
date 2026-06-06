package com.ams.core.dto;

import jakarta.validation.constraints.NotNull;

public record EnrollmentRequest(
    @NotNull(message = "Course assignment ID cannot be null")
    Long courseAssignmentId
) {}
