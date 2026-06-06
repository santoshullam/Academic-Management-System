package com.ams.core.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record BulkAttendanceRequest(
    @NotNull(message = "Course assignment ID cannot be null")
    Long courseAssignmentId,

    @NotNull(message = "Date cannot be null")
    LocalDate date,

    @NotEmpty(message = "Attendance records list cannot be empty")
    List<AttendanceRecord> records
) {}
