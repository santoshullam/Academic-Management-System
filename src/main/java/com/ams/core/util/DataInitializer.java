package com.ams.core.util;

import com.ams.core.entity.*;
import com.ams.core.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final FacultyProfileRepository facultyProfileRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final CourseRepository courseRepository;
    private final CourseAssignmentRepository courseAssignmentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           DepartmentRepository departmentRepository,
                           FacultyProfileRepository facultyProfileRepository,
                           StudentProfileRepository studentProfileRepository,
                           CourseRepository courseRepository,
                           CourseAssignmentRepository courseAssignmentRepository,
                           EnrollmentRepository enrollmentRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.facultyProfileRepository = facultyProfileRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.courseRepository = courseRepository;
        this.courseAssignmentRepository = courseAssignmentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("==== LIST OF ALL USERS IN SYSTEM ====");
        userRepository.findAll().forEach(u -> System.out.println("User: " + u.getUsername() + ", Role: " + u.getRole() + ", Active: " + u.isActive()));
        System.out.println("=====================================");

        // Ensure passwords of existing seeded users are aligned with our test credentials
        userRepository.findByUsername("admin").ifPresent(u -> {
            u.setPassword(passwordEncoder.encode("Password123"));
            userRepository.save(u);
        });
        userRepository.findByUsername("teacher_foo").ifPresent(u -> {
            u.setPassword(passwordEncoder.encode("TeacherPassword123"));
            userRepository.save(u);
        });
        userRepository.findByUsername("john_doe").ifPresent(u -> {
            u.setPassword(passwordEncoder.encode("StudentPassword123"));
            userRepository.save(u);
        });

        // 1. Seed Department
        Department cs = null;
        if (departmentRepository.count() == 0) {
            cs = new Department();
            cs.setName("Computer Science");
            cs.setCode("CS");
            cs = departmentRepository.save(cs);
            System.out.println("Seeded default department: Computer Science (CS)");
        } else {
            cs = departmentRepository.findAll().get(0);
        }

        // 2. Seed Admin
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("Password123"));
            admin.setEmail("admin@university.edu");
            admin.setRole(Role.ADMIN);
            admin.setActive(true);
            userRepository.save(admin);
            System.out.println("Seeded default admin user: admin / Password123");
        }

        // 3. Seed Course
        Course dsCourse = null;
        if (courseRepository.count() == 0) {
            dsCourse = new Course();
            dsCourse.setCode("CS201");
            dsCourse.setTitle("Data Structures and Algorithms");
            dsCourse.setCredits(4);
            dsCourse.setDepartment(cs);
            dsCourse = courseRepository.save(dsCourse);
            System.out.println("Seeded course: CS201 - Data Structures");
        } else {
            dsCourse = courseRepository.findAll().get(0);
        }

        // 4. Seed Faculty Profile
        FacultyProfile teacher = null;
        if (facultyProfileRepository.count() == 0) {
            User fUser = new User();
            fUser.setUsername("teacher_foo");
            fUser.setEmail("teacher.foo@university.edu");
            fUser.setPassword(passwordEncoder.encode("TeacherPassword123"));
            fUser.setRole(Role.FACULTY);
            fUser.setActive(true);

            teacher = new FacultyProfile();
            teacher.setUser(fUser);
            teacher.setFirstName("Alan");
            teacher.setLastName("Turing");
            teacher.setEmployeeId("FAC20260001");
            teacher.setDesignation("Professor");
            teacher.setPhone("+9876543210");
            teacher.setJoiningDate(LocalDate.now());
            teacher.setDepartment(cs);

            teacher = facultyProfileRepository.save(teacher);
            System.out.println("Seeded faculty profile: teacher_foo / TeacherPassword123");
        } else {
            teacher = facultyProfileRepository.findAll().get(0);
        }

        // 5. Seed Student Profile
        StudentProfile student = null;
        if (studentProfileRepository.count() == 0) {
            User sUser = new User();
            sUser.setUsername("student_bar");
            sUser.setEmail("student.bar@university.edu");
            sUser.setPassword(passwordEncoder.encode("StudentPassword123"));
            sUser.setRole(Role.STUDENT);
            sUser.setActive(true);

            student = new StudentProfile();
            student.setUser(sUser);
            student.setFirstName("Ada");
            student.setLastName("Lovelace");
            student.setRollNumber("STU20260001");
            student.setPhone("+1234567890");
            student.setDateOfBirth(LocalDate.of(2005, 12, 10));
            student.setEnrollmentDate(LocalDate.now());
            student.setDepartment(cs);

            student = studentProfileRepository.save(student);
            System.out.println("Seeded student profile: student_bar / StudentPassword123");
        } else {
            student = studentProfileRepository.findAll().get(0);
        }

        // 6. Seed Course Assignment
        CourseAssignment assignment = null;
        if (courseAssignmentRepository.count() == 0) {
            assignment = new CourseAssignment();
            assignment.setCourse(dsCourse);
            assignment.setFaculty(teacher);
            assignment.setSemester("Fall 2026");
            assignment.setAcademicYear("2026-2027");

            assignment = courseAssignmentRepository.save(assignment);
            System.out.println("Seeded course assignment: CS201 assigned to Alan Turing");
        } else {
            assignment = courseAssignmentRepository.findAll().get(0);
        }

        // 7. Seed Enrollment
        if (enrollmentRepository.count() == 0) {
            Enrollment enrollment = new Enrollment();
            enrollment.setStudent(student);
            enrollment.setCourseAssignment(assignment);
            enrollment.setEnrollmentDate(LocalDate.now());
            enrollment.setStatus(EnrollmentStatus.ENROLLED);

            enrollmentRepository.save(enrollment);
            System.out.println("Seeded student enrollment: Ada Lovelace enrolled in Fall 2026 CS201");
        }
    }
}
