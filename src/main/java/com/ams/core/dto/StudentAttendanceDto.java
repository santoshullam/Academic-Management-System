package com.ams.core.dto;

public record StudentAttendanceDto(
    String courseCode,
    String courseTitle,
    int lecturesConducted,
    int lecturesAttended,
    double percentage,
    String status
) {}
