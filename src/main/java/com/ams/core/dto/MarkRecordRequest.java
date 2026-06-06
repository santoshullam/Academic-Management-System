package com.ams.core.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MarkRecordRequest(
    @NotNull(message = "Enrollment ID cannot be null")
    Long enrollmentId,

    @NotBlank(message = "Assessment name cannot be empty")
    String assessmentName,

    @NotNull(message = "Marks obtained cannot be null")
    @DecimalMin(value = "0.0", message = "Marks obtained must be at least 0")
    Double marksObtained,

    @NotNull(message = "Max marks cannot be null")
    @DecimalMin(value = "1.0", message = "Max marks must be at least 1")
    Double maxMarks,

    @NotNull(message = "Weightage percentage cannot be null")
    @DecimalMin(value = "0.0", message = "Weightage must be at least 0")
    @DecimalMax(value = "100.0", message = "Weightage cannot exceed 100")
    Double weightagePercentage
) {}
