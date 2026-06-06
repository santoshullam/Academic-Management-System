package com.ams.core.dto;

public record CourseAssignmentDto(
    Long id,
    Long courseId,
    String courseCode,
    String courseTitle,
    Long facultyId,
    String facultyName,
    String semester,
    String academicYear
) {}
