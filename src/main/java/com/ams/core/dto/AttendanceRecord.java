package com.ams.core.dto;

import com.ams.core.entity.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

public record AttendanceRecord(
    @NotNull(message = "Student ID cannot be null")
    Long studentId,

    @NotNull(message = "Attendance status cannot be null")
    AttendanceStatus status
) {}
