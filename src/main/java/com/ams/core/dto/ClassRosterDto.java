package com.ams.core.dto;

import java.util.List;

public record ClassRosterDto(
    Long courseAssignmentId,
    String courseCode,
    String courseTitle,
    String semester,
    String academicYear,
    String facultyName,
    List<ClassRosterStudentDto> students
) {}
