package com.ams.core.service;

import com.ams.core.dto.StudentCreationRequest;
import com.ams.core.dto.StudentProfileDto;
import com.ams.core.dto.StudentUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService {
    StudentProfileDto registerStudent(StudentCreationRequest request);
    StudentProfileDto updateStudent(Long id, StudentUpdateRequest request);
    StudentProfileDto getStudentProfileById(Long id);
    StudentProfileDto getStudentProfileByUsername(String username);
    Page<StudentProfileDto> searchAndListStudents(String query, Pageable pageable);
    void toggleStudentActiveStatus(Long id, boolean active);
    void deleteStudent(Long id);
    void enrollInCourse(String username, Long courseAssignmentId);
}

