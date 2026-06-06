package com.ams.core.dto;

import java.time.LocalDate;

public record StudentProfileDto(
    Long id,
    String rollNumber,
    String firstName,
    String lastName,
    String username,
    String email,
    String phone,
    LocalDate dateOfBirth,
    LocalDate enrollmentDate,
    Long departmentId,
    String departmentName,
    boolean isActive
) {}
