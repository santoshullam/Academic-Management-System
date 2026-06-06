package com.ams.core.repository;

import com.ams.core.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    @Query("SELECT e FROM Enrollment e JOIN FETCH e.student s JOIN FETCH s.user WHERE e.courseAssignment.id = :courseAssignmentId")
    List<Enrollment> findByCourseAssignmentIdWithStudent(@Param("courseAssignmentId") Long courseAssignmentId);

    Optional<Enrollment> findByStudentIdAndCourseAssignmentId(Long studentId, Long courseAssignmentId);

    @Query("SELECT e FROM Enrollment e JOIN FETCH e.courseAssignment ca JOIN FETCH ca.course JOIN FETCH ca.faculty WHERE e.student.id = :studentId")
    List<Enrollment> findByStudentIdWithCourseAndFaculty(@Param("studentId") Long studentId);
}


