package com.ams.core.repository;

import com.ams.core.entity.CourseAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseAssignmentRepository extends JpaRepository<CourseAssignment, Long> {

    boolean existsByCourseIdAndFacultyIdAndSemesterAndAcademicYear(Long courseId, Long facultyId, String semester, String academicYear);

    @Query("SELECT ca FROM CourseAssignment ca JOIN FETCH ca.course JOIN FETCH ca.faculty f JOIN FETCH f.user JOIN FETCH ca.course.department WHERE f.user.username = :username")
    List<CourseAssignment> findByFacultyUserUsername(@Param("username") String username);

    @Query("SELECT ca FROM CourseAssignment ca JOIN FETCH ca.course JOIN FETCH ca.faculty f JOIN FETCH f.user JOIN FETCH ca.course.department WHERE ca.id = :id")
    Optional<CourseAssignment> findByIdWithCourseAndFaculty(@Param("id") Long id);

    @Query("SELECT ca FROM CourseAssignment ca JOIN FETCH ca.course JOIN FETCH ca.faculty f JOIN FETCH f.user JOIN FETCH ca.course.department")
    List<CourseAssignment> findAllWithCourseAndFaculty();
}
