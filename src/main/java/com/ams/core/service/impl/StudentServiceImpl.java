package com.ams.core.service.impl;

import com.ams.core.dto.StudentCreationRequest;
import com.ams.core.dto.StudentProfileDto;
import com.ams.core.dto.StudentUpdateRequest;
import com.ams.core.entity.Department;
import com.ams.core.entity.Role;
import com.ams.core.entity.StudentProfile;
import com.ams.core.entity.User;
import com.ams.core.entity.CourseAssignment;
import com.ams.core.entity.Enrollment;
import com.ams.core.entity.EnrollmentStatus;
import com.ams.core.exception.BadRequestException;
import com.ams.core.exception.ResourceNotFoundException;
import com.ams.core.repository.DepartmentRepository;
import com.ams.core.repository.StudentProfileRepository;
import com.ams.core.repository.UserRepository;
import com.ams.core.repository.CourseAssignmentRepository;
import com.ams.core.repository.EnrollmentRepository;
import com.ams.core.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentProfileRepository studentProfileRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final CourseAssignmentRepository courseAssignmentRepository;
    private final EnrollmentRepository enrollmentRepository;

    public StudentServiceImpl(StudentProfileRepository studentProfileRepository,
                              UserRepository userRepository,
                              DepartmentRepository departmentRepository,
                              PasswordEncoder passwordEncoder,
                              CourseAssignmentRepository courseAssignmentRepository,
                              EnrollmentRepository enrollmentRepository) {
        this.studentProfileRepository = studentProfileRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
        this.courseAssignmentRepository = courseAssignmentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }


    @Override
    @Transactional
    public StudentProfileDto registerStudent(StudentCreationRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new BadRequestException("Username is already taken");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email is already taken");
        }

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + request.departmentId()));

        // Create base security credentials
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.STUDENT);
        user.setActive(true);

        // Create detailed student profile
        StudentProfile studentProfile = new StudentProfile();
        studentProfile.setUser(user);
        studentProfile.setFirstName(request.firstName());
        studentProfile.setLastName(request.lastName());
        studentProfile.setPhone(request.phone());
        studentProfile.setDateOfBirth(request.dateOfBirth());
        studentProfile.setEnrollmentDate(LocalDate.now());
        studentProfile.setDepartment(department);

        // Auto-generate roll number: STU + Year + 4 digit sequence (e.g. STU20260001)
        String prefix = "STU" + LocalDate.now().getYear();
        Optional<StudentProfile> lastStudentOpt = studentProfileRepository.findFirstByRollNumberStartingWithOrderByRollNumberDesc(prefix);
        int nextSequenceId = 1;
        if (lastStudentOpt.isPresent()) {
            String lastRollNumber = lastStudentOpt.get().getRollNumber();
            try {
                String sequencePart = lastRollNumber.substring(prefix.length());
                nextSequenceId = Integer.parseInt(sequencePart) + 1;
            } catch (NumberFormatException e) {
                // Keep nextSequenceId = 1 in case of unexpected suffix
            }
        }
        String rollNumber = prefix + String.format("%04d", nextSequenceId);
        studentProfile.setRollNumber(rollNumber);

        StudentProfile savedProfile = studentProfileRepository.save(studentProfile);
        return convertToDto(savedProfile);
    }

    @Override
    @Transactional
    public StudentProfileDto updateStudent(Long id, StudentUpdateRequest request) {
        StudentProfile studentProfile = studentProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + request.departmentId()));

        studentProfile.setFirstName(request.firstName());
        studentProfile.setLastName(request.lastName());
        studentProfile.setPhone(request.phone());
        studentProfile.setDateOfBirth(request.dateOfBirth());
        studentProfile.setDepartment(department);

        StudentProfile updatedProfile = studentProfileRepository.save(studentProfile);
        return convertToDto(updatedProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentProfileDto getStudentProfileById(Long id) {
        StudentProfile studentProfile = studentProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        return convertToDto(studentProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentProfileDto getStudentProfileByUsername(String username) {
        StudentProfile studentProfile = studentProfileRepository.findByUserUsernameWithUserAndDepartment(username)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for username: " + username));
        return convertToDto(studentProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentProfileDto> searchAndListStudents(String query, Pageable pageable) {
        Page<StudentProfile> profiles;
        if (StringUtils.hasText(query)) {
            profiles = studentProfileRepository.searchStudents(query, pageable);
        } else {
            profiles = studentProfileRepository.findAllWithUserAndDepartment(pageable);
        }
        return profiles.map(this::convertToDto);
    }

    @Override
    @Transactional
    public void toggleStudentActiveStatus(Long id, boolean active) {
        StudentProfile studentProfile = studentProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        User user = studentProfile.getUser();
        user.setActive(active);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        StudentProfile studentProfile = studentProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        
        // Delete related enrollments to prevent foreign key constraint violation
        enrollmentRepository.deleteByStudentId(id);

        // Deleting studentProfile cascades to delete User credentials since cascade=CascadeType.ALL is configured.
        studentProfileRepository.delete(studentProfile);
    }

    @Override
    @Transactional
    public void enrollInCourse(String username, Long courseAssignmentId) {
        StudentProfile student = studentProfileRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for user: " + username));

        CourseAssignment courseAssignment = courseAssignmentRepository.findById(courseAssignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Course assignment not found with ID: " + courseAssignmentId));

        if (enrollmentRepository.findByStudentIdAndCourseAssignmentId(student.getId(), courseAssignmentId).isPresent()) {
            throw new BadRequestException("You are already enrolled in this course section.");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourseAssignment(courseAssignment);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus(EnrollmentStatus.ENROLLED);

        enrollmentRepository.save(enrollment);
    }

    private StudentProfileDto convertToDto(StudentProfile profile) {
        return new StudentProfileDto(
                profile.getId(),
                profile.getRollNumber(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getUser().getUsername(),
                profile.getUser().getEmail(),
                profile.getPhone(),
                profile.getDateOfBirth(),
                profile.getEnrollmentDate(),
                profile.getDepartment().getId(),
                profile.getDepartment().getName(),
                profile.getUser().isActive()
        );
    }
}
