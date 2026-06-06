package com.ams.core.repository;

import com.ams.core.entity.FacultyProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacultyProfileRepository extends JpaRepository<FacultyProfile, Long> {

    Optional<FacultyProfile> findByEmployeeId(String employeeId);

    boolean existsByEmployeeId(String employeeId);

    @Query("SELECT f FROM FacultyProfile f JOIN FETCH f.user JOIN FETCH f.department WHERE f.user.username = :username")
    Optional<FacultyProfile> findByUserUsernameWithUserAndDepartment(@Param("username") String username);

    Optional<FacultyProfile> findByUserUsername(String username);

    Optional<FacultyProfile> findFirstByEmployeeIdStartingWithOrderByEmployeeIdDesc(String prefix);

    @Query(value = "SELECT f FROM FacultyProfile f JOIN FETCH f.user JOIN FETCH f.department " +
            "WHERE LOWER(f.firstName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(f.lastName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(f.employeeId) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(f.user.email) LIKE LOWER(CONCAT('%', :query, '%'))",
            countQuery = "SELECT COUNT(f) FROM FacultyProfile f WHERE LOWER(f.firstName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(f.lastName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(f.employeeId) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(f.user.email) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<FacultyProfile> searchFaculty(@Param("query") String query, Pageable pageable);

    @Query(value = "SELECT f FROM FacultyProfile f JOIN FETCH f.user JOIN FETCH f.department",
            countQuery = "SELECT COUNT(f) FROM FacultyProfile f")
    Page<FacultyProfile> findAllWithUserAndDepartment(Pageable pageable);
}
