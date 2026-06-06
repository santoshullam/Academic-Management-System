package com.ams.core.service;

import com.ams.core.dto.FacultyCreationRequest;
import com.ams.core.dto.FacultyProfileDto;
import com.ams.core.dto.FacultyUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FacultyService {
    FacultyProfileDto registerFaculty(FacultyCreationRequest request);
    FacultyProfileDto updateFaculty(Long id, FacultyUpdateRequest request);
    FacultyProfileDto getFacultyById(Long id);
    FacultyProfileDto getFacultyByUsername(String username);
    Page<FacultyProfileDto> searchAndListFaculty(String query, Pageable pageable);
    void toggleFacultyActiveStatus(Long id, boolean active);
    void deleteFaculty(Long id);
}
