package com.ams.core.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseCreationRequest(
    @NotBlank(message = "Course code cannot be empty")
    String code,

    @NotBlank(message = "Course title cannot be empty")
    String title,

    @Min(value = 1, message = "Credits must be at least 1")
    @Max(value = 5, message = "Credits cannot exceed 5")
    int credits,

    @NotNull(message = "Department ID cannot be null")
    Long departmentId
) {}
