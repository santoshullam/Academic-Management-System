package com.ams.core.service.impl;

import com.ams.core.dto.AttendanceRecord;
import com.ams.core.dto.BulkAttendanceRequest;
import com.ams.core.dto.StudentAttendanceDto;
import com.ams.core.entity.*;
import com.ams.core.exception.BadRequestException;
import com.ams.core.exception.ResourceNotFoundException;
import com.ams.core.repository.AttendanceRepository;
import com.ams.core.repository.CourseAssignmentRepository;
import com.ams.core.repository.EnrollmentRepository;
import com.ams.core.repository.FacultyProfileRepository;
import com.ams.core.repository.StudentProfileRepository;
import com.ams.core.service.AttendanceService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final CourseAssignmentRepository courseAssignmentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final FacultyProfileRepository facultyProfileRepository;
    private final StudentProfileRepository studentProfileRepository;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository,
                                 CourseAssignmentRepository courseAssignmentRepository,
                                 EnrollmentRepository enrollmentRepository,
                                 FacultyProfileRepository facultyProfileRepository,
                                 StudentProfileRepository studentProfileRepository) {
        this.attendanceRepository = attendanceRepository;
        this.courseAssignmentRepository = courseAssignmentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.facultyProfileRepository = facultyProfileRepository;
        this.studentProfileRepository = studentProfileRepository;
    }

    @Override
    @Transactional
    public void recordBulkAttendance(BulkAttendanceRequest request, String facultyUsername) {
        CourseAssignment courseAssignment = courseAssignmentRepository.findById(request.courseAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Course assignment not found with ID: " + request.courseAssignmentId()));

        // Security boundary: faculty must be assigned to this section
        FacultyProfile faculty = facultyProfileRepository.findByUserUsername(facultyUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty profile not found for user: " + facultyUsername));

        if (!courseAssignment.getFaculty().getId().equals(faculty.getId())) {
            throw new AccessDeniedException("Access denied: You are not assigned to teach this course section.");
        }

        for (AttendanceRecord record : request.records()) {
            Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseAssignmentId(record.studentId(), request.courseAssignmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student with ID " + record.studentId() + " is not enrolled in this course section."));

            Optional<Attendance> existingAttendance = attendanceRepository.findByEnrollmentIdAndDate(enrollment.getId(), request.date());
            if (existingAttendance.isPresent()) {
                Attendance attendance = existingAttendance.get();
                attendance.setStatus(record.status());
                attendanceRepository.save(attendance);
            } else {
                Attendance attendance = new Attendance();
                attendance.setEnrollment(enrollment);
                attendance.setDate(request.date());
                attendance.setStatus(record.status());
                attendanceRepository.save(attendance);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentAttendanceDto> getStudentAttendance(String studentUsername) {
        StudentProfile student = studentProfileRepository.findByUserUsername(studentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for user: " + studentUsername));

        List<Enrollment> enrollments = enrollmentRepository.findByStudentIdWithCourseAndFaculty(student.getId());
        List<Attendance> attendances = attendanceRepository.findByStudentIdWithCourse(student.getId());

        // Group attendances by enrollment ID
        Map<Long, List<Attendance>> attendanceMap = attendances.stream()
                .collect(Collectors.groupingBy(a -> a.getEnrollment().getId()));

        List<StudentAttendanceDto> dtoList = new ArrayList<>();
        for (Enrollment enrollment : enrollments) {
            List<Attendance> recordList = attendanceMap.getOrDefault(enrollment.getId(), List.of());

            int lecturesConducted = recordList.size();
            int lecturesAttended = (int) recordList.stream()
                    .filter(a -> a.getStatus() == AttendanceStatus.PRESENT)
                    .count();

            double percentage = lecturesConducted == 0 ? 100.0 : ((double) lecturesAttended * 100.0) / lecturesConducted;
            // Round to 2 decimal places
            percentage = Math.round(percentage * 100.0) / 100.0;

            String status = percentage < 75.0 ? "WARNING" : "SAFE";

            dtoList.add(new StudentAttendanceDto(
                    enrollment.getCourseAssignment().getCourse().getCode(),
                    enrollment.getCourseAssignment().getCourse().getTitle(),
                    lecturesConducted,
                    lecturesAttended,
                    percentage,
                    status
            ));
        }

        return dtoList;
    }
}
