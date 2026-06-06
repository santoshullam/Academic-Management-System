package com.ams.core.service;

import com.ams.core.dto.BulkAttendanceRequest;
import com.ams.core.dto.StudentAttendanceDto;

import java.util.List;

public interface AttendanceService {
    void recordBulkAttendance(BulkAttendanceRequest request, String facultyUsername);
    List<StudentAttendanceDto> getStudentAttendance(String studentUsername);
}
