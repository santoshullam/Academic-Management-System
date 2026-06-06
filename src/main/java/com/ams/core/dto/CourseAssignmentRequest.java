package com.ams.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseAssignmentRequest(
    @NotNull(message = "Course ID cannot be null")
    Long courseId,

    @NotNull(message = "Faculty ID cannot be null")
    Long facultyId,

    @NotBlank(message = "Semester cannot be empty")
    String semester,

    @NotBlank(message = "Academic year cannot be empty")
    String academicYear
) {}
