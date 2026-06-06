package com.ams.core.service.impl;

import com.ams.core.dto.CourseCreationRequest;
import com.ams.core.dto.CourseDto;
import com.ams.core.dto.CourseUpdateRequest;
import com.ams.core.entity.Course;
import com.ams.core.entity.Department;
import com.ams.core.exception.BadRequestException;
import com.ams.core.exception.ResourceNotFoundException;
import com.ams.core.repository.CourseRepository;
import com.ams.core.repository.DepartmentRepository;
import com.ams.core.service.CourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;

    public CourseServiceImpl(CourseRepository courseRepository, DepartmentRepository departmentRepository) {
        this.courseRepository = courseRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    @Transactional
    public CourseDto createCourse(CourseCreationRequest request) {
        if (courseRepository.existsByCode(request.code())) {
            throw new BadRequestException("Course code is already taken");
        }

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + request.departmentId()));

        Course course = new Course();
        course.setCode(request.code());
        course.setTitle(request.title());
        course.setCredits(request.credits());
        course.setDepartment(department);

        Course saved = courseRepository.save(course);
        return convertToDto(saved);
    }

    @Override
    @Transactional
    public CourseDto updateCourse(Long id, CourseUpdateRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + request.departmentId()));

        course.setTitle(request.title());
        course.setCredits(request.credits());
        course.setDepartment(department);

        Course updated = courseRepository.save(course);
        return convertToDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDto getCourseById(Long id) {
        Course course = courseRepository.findByIdWithDepartment(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));
        return convertToDto(course);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDto> getAllCourses() {
        return courseRepository.findAllWithDepartment().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course not found with ID: " + id);
        }
        courseRepository.deleteById(id);
    }

    private CourseDto convertToDto(Course course) {
        return new CourseDto(
                course.getId(),
                course.getCode(),
                course.getTitle(),
                course.getCredits(),
                course.getDepartment().getId(),
                course.getDepartment().getName()
        );
    }
}
