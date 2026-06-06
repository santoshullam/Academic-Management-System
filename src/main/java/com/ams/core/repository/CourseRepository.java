package com.ams.core.repository;

import com.ams.core.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT c FROM Course c JOIN FETCH c.department WHERE c.id = :id")
    Optional<Course> findByIdWithDepartment(@Param("id") Long id);

    @Query("SELECT c FROM Course c JOIN FETCH c.department")
    List<Course> findAllWithDepartment();
}
