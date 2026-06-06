package com.ams.core.controller;

import com.ams.core.dto.ApiResponse;
import com.ams.core.dto.StudentCreationRequest;
import com.ams.core.dto.StudentProfileDto;
import com.ams.core.dto.StudentUpdateRequest;
import com.ams.core.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/students")
@PreAuthorize("hasRole('ADMIN')")
public class AdminStudentController {

    private final StudentService studentService;

    public AdminStudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentProfileDto>> registerStudent(@Valid @RequestBody StudentCreationRequest request) {
        StudentProfileDto responseData = studentService.registerStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Student registered successfully", responseData));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentProfileDto>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentUpdateRequest request) {
        StudentProfileDto responseData = studentService.updateStudent(id, request);
        return ResponseEntity.ok(ApiResponse.success("Student updated successfully", responseData));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<StudentProfileDto>>> getStudents(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {
        
        Sort.Direction direction = Sort.Direction.fromString(sortDir.toLowerCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<StudentProfileDto> responseData = studentService.searchAndListStudents(query, pageable);
        return ResponseEntity.ok(ApiResponse.success("Students retrieved successfully", responseData));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> toggleStatus(
            @PathVariable Long id,
            @RequestParam("active") boolean active) {
        studentService.toggleStudentActiveStatus(id, active);
        String message = active ? "Student account enabled" : "Student account disabled";
        return ResponseEntity.ok(ApiResponse.success(message, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success("Student deleted successfully", null));
    }
}
