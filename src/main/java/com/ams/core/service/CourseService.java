package com.ams.core.service;

import com.ams.core.dto.CourseCreationRequest;
import com.ams.core.dto.CourseDto;
import com.ams.core.dto.CourseUpdateRequest;

import java.util.List;

public interface CourseService {
    CourseDto createCourse(CourseCreationRequest request);
    CourseDto updateCourse(Long id, CourseUpdateRequest request);
    CourseDto getCourseById(Long id);
    List<CourseDto> getAllCourses();
    void deleteCourse(Long id);
}
