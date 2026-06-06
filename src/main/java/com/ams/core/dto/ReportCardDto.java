package com.ams.core.dto;

import java.util.List;

public record ReportCardDto(
    String studentName,
    String rollNumber,
    List<CourseGradeDto> courses,
    double cgpa
) {}
