package com.ams.core.dto;

public record CourseDto(
    Long id,
    String code,
    String title,
    int credits,
    Long departmentId,
    String departmentName
) {}
