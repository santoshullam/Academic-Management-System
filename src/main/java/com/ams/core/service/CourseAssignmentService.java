package com.ams.core.service;

import com.ams.core.dto.ClassRosterDto;
import com.ams.core.dto.CourseAssignmentDto;
import com.ams.core.dto.CourseAssignmentRequest;

import java.util.List;

public interface CourseAssignmentService {
    CourseAssignmentDto assignCourse(CourseAssignmentRequest request);
    List<CourseAssignmentDto> getFacultyWorkload(String facultyUsername);
    List<CourseAssignmentDto> getAllAssignments();
    void deleteAssignment(Long id);
    ClassRosterDto getClassRoster(Long courseAssignmentId, String facultyUsername);
}
