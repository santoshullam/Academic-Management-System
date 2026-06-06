package com.ams.core.controller;

import com.ams.core.dto.ApiResponse;
import com.ams.core.dto.CourseAssignmentDto;
import com.ams.core.dto.CourseAssignmentRequest;
import com.ams.core.service.CourseAssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminCourseAssignmentController {

    private final CourseAssignmentService courseAssignmentService;

    public AdminCourseAssignmentController(CourseAssignmentService courseAssignmentService) {
        this.courseAssignmentService = courseAssignmentService;
    }

    @PostMapping({"/api/admin/course-assignments", "/api/courses/assign"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CourseAssignmentDto>> assignCourse(@Valid @RequestBody CourseAssignmentRequest request) {
        CourseAssignmentDto responseData = courseAssignmentService.assignCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Course assigned to faculty successfully", responseData));
    }

    @GetMapping("/api/admin/course-assignments")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public ResponseEntity<ApiResponse<List<CourseAssignmentDto>>> getAllAssignments() {
        List<CourseAssignmentDto> responseData = courseAssignmentService.getAllAssignments();
        return ResponseEntity.ok(ApiResponse.success("Course assignments retrieved successfully", responseData));
    }

    @DeleteMapping("/api/admin/course-assignments/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteAssignment(@PathVariable Long id) {
        courseAssignmentService.deleteAssignment(id);
        return ResponseEntity.ok(ApiResponse.success("Course assignment deleted successfully", null));
    }
}
