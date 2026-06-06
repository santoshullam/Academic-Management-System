package com.ams.core.controller;

import com.ams.core.dto.ApiResponse;
import com.ams.core.dto.BulkAttendanceRequest;
import com.ams.core.dto.StudentAttendanceDto;
import com.ams.core.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/faculty/attendance")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<ApiResponse<Void>> recordAttendance(@Valid @RequestBody BulkAttendanceRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        attendanceService.recordBulkAttendance(request, username);
        return ResponseEntity.ok(ApiResponse.success("Attendance recorded successfully", null));
    }

    @GetMapping("/students/attendance")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<StudentAttendanceDto>>> getAttendanceStats() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<StudentAttendanceDto> stats = attendanceService.getStudentAttendance(username);
        return ResponseEntity.ok(ApiResponse.success("Attendance stats retrieved successfully", stats));
    }
}
