package com.ams.core.dto;

import java.time.LocalDate;

public record FacultyProfileDto(
    Long id,
    String employeeId,
    String firstName,
    String lastName,
    String username,
    String email,
    String phone,
    String designation,
    LocalDate joiningDate,
    Long departmentId,
    String departmentName,
    boolean isActive
) {}
