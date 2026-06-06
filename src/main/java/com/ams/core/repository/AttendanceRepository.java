package com.ams.core.repository;

import com.ams.core.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByEnrollmentIdAndDate(Long enrollmentId, LocalDate date);

    @Query("SELECT a FROM Attendance a JOIN FETCH a.enrollment e JOIN FETCH e.courseAssignment ca JOIN FETCH ca.course WHERE e.student.id = :studentId")
    List<Attendance> findByStudentIdWithCourse(@Param("studentId") Long studentId);
}
