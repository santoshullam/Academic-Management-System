package com.ams.core.controller;

import com.ams.core.dto.ApiResponse;
import com.ams.core.dto.MarkRecordRequest;
import com.ams.core.dto.MarkRecordResponse;
import com.ams.core.dto.ReportCardDto;
import com.ams.core.service.GradeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping("/faculty/marks")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<ApiResponse<MarkRecordResponse>> recordMark(@Valid @RequestBody MarkRecordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        MarkRecordResponse response = gradeService.recordStudentMark(request, username);
        return ResponseEntity.ok(ApiResponse.success("Mark recorded successfully", response));
    }

    @GetMapping("/students/report-card")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<ReportCardDto>> getReportCard() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        ReportCardDto response = gradeService.getStudentReportCard(username);
        return ResponseEntity.ok(ApiResponse.success("Report card retrieved successfully", response));
    }
}
