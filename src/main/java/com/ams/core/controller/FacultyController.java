package com.ams.core.controller;

import com.ams.core.dto.ApiResponse;
import com.ams.core.dto.ClassRosterDto;
import com.ams.core.dto.CourseAssignmentDto;
import com.ams.core.service.CourseAssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/faculty")
@PreAuthorize("hasRole('FACULTY')")
public class FacultyController {

    private final CourseAssignmentService courseAssignmentService;

    public FacultyController(CourseAssignmentService courseAssignmentService) {
        this.courseAssignmentService = courseAssignmentService;
    }

    @GetMapping("/assignments")
    public ResponseEntity<ApiResponse<List<CourseAssignmentDto>>> getOwnAssignments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<CourseAssignmentDto> responseData = courseAssignmentService.getFacultyWorkload(username);
        return ResponseEntity.ok(ApiResponse.success("Assigned courses retrieved successfully", responseData));
    }

    @GetMapping("/rosters/{courseAssignmentId}")
    public ResponseEntity<ApiResponse<ClassRosterDto>> getClassRoster(@PathVariable Long courseAssignmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        ClassRosterDto responseData = courseAssignmentService.getClassRoster(courseAssignmentId, username);
        return ResponseEntity.ok(ApiResponse.success("Class roster retrieved successfully", responseData));
    }
}
