package com.ams.core.service.impl;

import com.ams.core.dto.CourseGradeDto;
import com.ams.core.dto.MarkRecordRequest;
import com.ams.core.dto.MarkRecordResponse;
import com.ams.core.dto.ReportCardDto;
import com.ams.core.entity.Enrollment;
import com.ams.core.entity.FacultyProfile;
import com.ams.core.entity.Mark;
import com.ams.core.entity.StudentProfile;
import com.ams.core.exception.BadRequestException;
import com.ams.core.exception.ResourceNotFoundException;
import com.ams.core.repository.EnrollmentRepository;
import com.ams.core.repository.FacultyProfileRepository;
import com.ams.core.repository.MarkRepository;
import com.ams.core.repository.StudentProfileRepository;
import com.ams.core.service.GradeService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GradeServiceImpl implements GradeService {

    private final MarkRepository markRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final FacultyProfileRepository facultyProfileRepository;
    private final StudentProfileRepository studentProfileRepository;

    public GradeServiceImpl(MarkRepository markRepository,
                            EnrollmentRepository enrollmentRepository,
                            FacultyProfileRepository facultyProfileRepository,
                            StudentProfileRepository studentProfileRepository) {
        this.markRepository = markRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.facultyProfileRepository = facultyProfileRepository;
        this.studentProfileRepository = studentProfileRepository;
    }

    @Override
    @Transactional
    public MarkRecordResponse recordStudentMark(MarkRecordRequest request, String facultyUsername) {
        Enrollment enrollment = enrollmentRepository.findById(request.enrollmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with ID: " + request.enrollmentId()));

        // Security boundary: faculty must be assigned to teach this course section
        FacultyProfile faculty = facultyProfileRepository.findByUserUsername(facultyUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty profile not found for user: " + facultyUsername));

        if (!enrollment.getCourseAssignment().getFaculty().getId().equals(faculty.getId())) {
            throw new AccessDeniedException("Access denied: You are not assigned to teach this course section.");
        }

        // Marks validation
        if (request.marksObtained() > request.maxMarks()) {
            throw new BadRequestException("Marks obtained cannot exceed maximum marks.");
        }

        Optional<Mark> existingMarkOpt = markRepository.findByEnrollmentIdAndAssessmentName(enrollment.getId(), request.assessmentName());
        Mark mark;
        if (existingMarkOpt.isPresent()) {
            mark = existingMarkOpt.get();
            mark.setMarksObtained(request.marksObtained());
            mark.setMaxMarks(request.maxMarks());
            mark.setWeightagePercentage(request.weightagePercentage());
        } else {
            mark = new Mark();
            mark.setEnrollment(enrollment);
            mark.setAssessmentName(request.assessmentName());
            mark.setMarksObtained(request.marksObtained());
            mark.setMaxMarks(request.maxMarks());
            mark.setWeightagePercentage(request.weightagePercentage());
        }

        Mark saved = markRepository.save(mark);
        double weightedScore = (saved.getMarksObtained() / saved.getMaxMarks()) * saved.getWeightagePercentage();
        // Round to 2 decimal places
        weightedScore = Math.round(weightedScore * 100.0) / 100.0;

        return new MarkRecordResponse(saved.getId(), weightedScore, saved.getAssessmentName());
    }

    @Override
    @Transactional(readOnly = true)
    public ReportCardDto getStudentReportCard(String studentUsername) {
        StudentProfile student = studentProfileRepository.findByUserUsername(studentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for user: " + studentUsername));

        List<Enrollment> enrollments = enrollmentRepository.findByStudentIdWithCourseAndFaculty(student.getId());
        List<Mark> marks = markRepository.findByStudentIdWithCourse(student.getId());

        // Group marks by enrollment ID
        Map<Long, List<Mark>> markMap = marks.stream()
                .collect(Collectors.groupingBy(m -> m.getEnrollment().getId()));

        List<CourseGradeDto> courseGrades = new ArrayList<>();
        double totalWeightedGradePoints = 0.0;
        int totalCredits = 0;

        for (Enrollment enrollment : enrollments) {
            List<Mark> enrollmentMarks = markMap.getOrDefault(enrollment.getId(), List.of());

            double totalScore = 0.0;
            for (Mark m : enrollmentMarks) {
                double score = (m.getMarksObtained() / m.getMaxMarks()) * m.getWeightagePercentage();
                totalScore += score;
            }
            totalScore = Math.round(totalScore * 100.0) / 100.0;

            String grade = getLetterGrade(totalScore);
            double gradePoint = getGradePoint(grade);
            int credits = enrollment.getCourseAssignment().getCourse().getCredits();

            courseGrades.add(new CourseGradeDto(
                    enrollment.getCourseAssignment().getCourse().getCode(),
                    enrollment.getCourseAssignment().getCourse().getTitle(),
                    totalScore,
                    grade,
                    credits
            ));

            totalWeightedGradePoints += gradePoint * credits;
            totalCredits += credits;
        }

        double cgpa = totalCredits == 0 ? 0.0 : totalWeightedGradePoints / totalCredits;
        cgpa = Math.round(cgpa * 100.0) / 100.0;

        String studentFullName = student.getFirstName() + " " + student.getLastName();

        return new ReportCardDto(
                studentFullName,
                student.getRollNumber(),
                courseGrades,
                cgpa
        );
    }

    private String getLetterGrade(double totalScore) {
        if (totalScore >= 90.0) {
            return "A";
        } else if (totalScore >= 80.0) {
            return "B";
        } else if (totalScore >= 70.0) {
            return "C";
        } else if (totalScore >= 60.0) {
            return "D";
        } else {
            return "F";
        }
    }

    private double getGradePoint(String grade) {
        switch (grade) {
            case "A": return 4.0;
            case "B": return 3.0;
            case "C": return 2.0;
            case "D": return 1.0;
            default: return 0.0;
        }
    }
}
