package com.ams.core.service.impl;

import com.ams.core.dto.ClassRosterDto;
import com.ams.core.dto.ClassRosterStudentDto;
import com.ams.core.dto.CourseAssignmentDto;
import com.ams.core.dto.CourseAssignmentRequest;
import com.ams.core.entity.Course;
import com.ams.core.entity.CourseAssignment;
import com.ams.core.entity.Enrollment;
import com.ams.core.entity.FacultyProfile;
import com.ams.core.exception.BadRequestException;
import com.ams.core.exception.ResourceNotFoundException;
import com.ams.core.repository.CourseAssignmentRepository;
import com.ams.core.repository.CourseRepository;
import com.ams.core.repository.EnrollmentRepository;
import com.ams.core.repository.FacultyProfileRepository;
import com.ams.core.service.CourseAssignmentService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseAssignmentServiceImpl implements CourseAssignmentService {

    private final CourseAssignmentRepository courseAssignmentRepository;
    private final CourseRepository courseRepository;
    private final FacultyProfileRepository facultyProfileRepository;
    private final EnrollmentRepository enrollmentRepository;

    public CourseAssignmentServiceImpl(CourseAssignmentRepository courseAssignmentRepository,
                                       CourseRepository courseRepository,
                                       FacultyProfileRepository facultyProfileRepository,
                                       EnrollmentRepository enrollmentRepository) {
        this.courseAssignmentRepository = courseAssignmentRepository;
        this.courseRepository = courseRepository;
        this.facultyProfileRepository = facultyProfileRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    @Transactional
    public CourseAssignmentDto assignCourse(CourseAssignmentRequest request) {
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + request.courseId()));

        FacultyProfile faculty = facultyProfileRepository.findById(request.facultyId())
                .orElseThrow(() -> new ResourceNotFoundException("Faculty member not found with ID: " + request.facultyId()));

        if (courseAssignmentRepository.existsByCourseIdAndFacultyIdAndSemesterAndAcademicYear(
                request.courseId(), request.facultyId(), request.semester(), request.academicYear())) {
            throw new BadRequestException("This faculty member is already assigned to this course for the selected semester.");
        }

        CourseAssignment assignment = new CourseAssignment();
        assignment.setCourse(course);
        assignment.setFaculty(faculty);
        assignment.setSemester(request.semester());
        assignment.setAcademicYear(request.academicYear());

        CourseAssignment saved = courseAssignmentRepository.save(assignment);
        return convertToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseAssignmentDto> getFacultyWorkload(String facultyUsername) {
        return courseAssignmentRepository.findByFacultyUserUsername(facultyUsername).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseAssignmentDto> getAllAssignments() {
        return courseAssignmentRepository.findAllWithCourseAndFaculty().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAssignment(Long id) {
        if (!courseAssignmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course assignment not found with ID: " + id);
        }
        courseAssignmentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ClassRosterDto getClassRoster(Long courseAssignmentId, String facultyUsername) {
        CourseAssignment courseAssignment = courseAssignmentRepository.findByIdWithCourseAndFaculty(courseAssignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Course assignment not found with ID: " + courseAssignmentId));

        // Security boundary validation: check if the logged-in faculty is assigned to this section
        String assignedFacultyUsername = courseAssignment.getFaculty().getUser().getUsername();
        if (!assignedFacultyUsername.equalsIgnoreCase(facultyUsername)) {
            throw new AccessDeniedException("Access denied: You are not assigned to teach this course section.");
        }

        List<Enrollment> enrollments = enrollmentRepository.findByCourseAssignmentIdWithStudent(courseAssignmentId);

        List<ClassRosterStudentDto> studentDtos = enrollments.stream()
                .map(e -> new ClassRosterStudentDto(
                        e.getStudent().getId(),
                        e.getId(),
                        e.getStudent().getRollNumber(),
                        e.getStudent().getFirstName(),
                        e.getStudent().getLastName(),
                        e.getStudent().getUser().getEmail()
                ))
                .collect(Collectors.toList());

        String facultyFullName = courseAssignment.getFaculty().getFirstName() + " " + courseAssignment.getFaculty().getLastName();

        return new ClassRosterDto(
                courseAssignment.getId(),
                courseAssignment.getCourse().getCode(),
                courseAssignment.getCourse().getTitle(),
                courseAssignment.getSemester(),
                courseAssignment.getAcademicYear(),
                facultyFullName,
                studentDtos
        );
    }

    private CourseAssignmentDto convertToDto(CourseAssignment assignment) {
        String facultyFullName = assignment.getFaculty().getFirstName() + " " + assignment.getFaculty().getLastName();
        return new CourseAssignmentDto(
                assignment.getId(),
                assignment.getCourse().getId(),
                assignment.getCourse().getCode(),
                assignment.getCourse().getTitle(),
                assignment.getFaculty().getId(),
                facultyFullName,
                assignment.getSemester(),
                assignment.getAcademicYear()
        );
    }
}
