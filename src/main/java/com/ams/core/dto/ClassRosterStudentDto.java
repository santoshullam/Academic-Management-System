package com.ams.core.dto;

public record ClassRosterStudentDto(
    Long studentId,
    Long enrollmentId,
    String rollNumber,
    String firstName,
    String lastName,
    String email
) {}
