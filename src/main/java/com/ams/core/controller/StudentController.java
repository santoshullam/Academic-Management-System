package com.ams.core.controller;

import com.ams.core.dto.ApiResponse;
import com.ams.core.dto.StudentProfileDto;
import com.ams.core.dto.EnrollmentRequest;
import com.ams.core.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<StudentProfileDto>> getOwnProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        StudentProfileDto responseData = studentService.getStudentProfileByUsername(username);
        return ResponseEntity.ok(ApiResponse.success("Profile fetched successfully", responseData));
    }

    @PostMapping("/enroll")
    public ResponseEntity<ApiResponse<Void>> enrollInCourse(@Valid @RequestBody EnrollmentRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        studentService.enrollInCourse(username, request.courseAssignmentId());
        return ResponseEntity.ok(ApiResponse.success("Enrolled in course successfully", null));
    }
}

