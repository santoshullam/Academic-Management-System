package com.ams.core.repository;

import com.ams.core.entity.StudentProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {

    @Query("SELECT s FROM StudentProfile s JOIN FETCH s.user JOIN FETCH s.department WHERE s.rollNumber = :rollNumber")
    Optional<StudentProfile> findByRollNumberWithUserAndDepartment(@Param("rollNumber") String rollNumber);

    Optional<StudentProfile> findByRollNumber(String rollNumber);

    boolean existsByRollNumber(String rollNumber);

    @Query("SELECT s FROM StudentProfile s JOIN FETCH s.user JOIN FETCH s.department WHERE s.user.username = :username")
    Optional<StudentProfile> findByUserUsernameWithUserAndDepartment(@Param("username") String username);

    Optional<StudentProfile> findByUserUsername(String username);

    Optional<StudentProfile> findFirstByRollNumberStartingWithOrderByRollNumberDesc(String prefix);

    @Query(value = "SELECT s FROM StudentProfile s JOIN FETCH s.user JOIN FETCH s.department " +
            "WHERE LOWER(s.firstName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(s.rollNumber) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(s.user.email) LIKE LOWER(CONCAT('%', :query, '%'))",
            countQuery = "SELECT COUNT(s) FROM StudentProfile s WHERE LOWER(s.firstName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(s.rollNumber) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(s.user.email) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<StudentProfile> searchStudents(@Param("query") String query, Pageable pageable);

    @Query(value = "SELECT s FROM StudentProfile s JOIN FETCH s.user JOIN FETCH s.department",
            countQuery = "SELECT COUNT(s) FROM StudentProfile s")
    Page<StudentProfile> findAllWithUserAndDepartment(Pageable pageable);
}
