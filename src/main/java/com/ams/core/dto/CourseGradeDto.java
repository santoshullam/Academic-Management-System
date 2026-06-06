package com.ams.core.dto;

public record CourseGradeDto(
    String courseCode,
    String courseTitle,
    double totalScore,
    String grade,
    int credits
) {}
