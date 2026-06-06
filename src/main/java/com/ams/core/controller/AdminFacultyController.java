package com.ams.core.controller;

import com.ams.core.dto.ApiResponse;
import com.ams.core.dto.FacultyCreationRequest;
import com.ams.core.dto.FacultyProfileDto;
import com.ams.core.dto.FacultyUpdateRequest;
import com.ams.core.service.FacultyService;
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
@RequestMapping("/api/admin/faculty")
@PreAuthorize("hasRole('ADMIN')")
public class AdminFacultyController {

    private final FacultyService facultyService;

    public AdminFacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FacultyProfileDto>> registerFaculty(@Valid @RequestBody FacultyCreationRequest request) {
        FacultyProfileDto responseData = facultyService.registerFaculty(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(), "Faculty registered successfully", responseData));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FacultyProfileDto>> updateFaculty(
            @PathVariable Long id,
            @Valid @RequestBody FacultyUpdateRequest request) {
        FacultyProfileDto responseData = facultyService.updateFaculty(id, request);
        return ResponseEntity.ok(ApiResponse.success("Faculty updated successfully", responseData));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<FacultyProfileDto>>> getFaculty(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {

        Sort.Direction direction = Sort.Direction.fromString(sortDir.toLowerCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<FacultyProfileDto> responseData = facultyService.searchAndListFaculty(query, pageable);
        return ResponseEntity.ok(ApiResponse.success("Faculty records retrieved successfully", responseData));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> toggleStatus(
            @PathVariable Long id,
            @RequestParam("active") boolean active) {
        facultyService.toggleFacultyActiveStatus(id, active);
        String message = active ? "Faculty account enabled" : "Faculty account disabled";
        return ResponseEntity.ok(ApiResponse.success(message, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok(ApiResponse.success("Faculty deleted successfully", null));
    }
}
