package com.ams.core.repository;

import com.ams.core.entity.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {

    Optional<Mark> findByEnrollmentIdAndAssessmentName(Long enrollmentId, String assessmentName);

    @Query("SELECT m FROM Mark m JOIN FETCH m.enrollment e JOIN FETCH e.courseAssignment ca JOIN FETCH ca.course WHERE e.student.id = :studentId")
    List<Mark> findByStudentIdWithCourse(@Param("studentId") Long studentId);
}
