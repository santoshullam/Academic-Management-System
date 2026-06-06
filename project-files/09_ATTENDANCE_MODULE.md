# 09 Attendance Module - Academic Management System (AMS)

## 1. Purpose of the File
This file guides the implementation of the Attendance Management Module, which tracks daily student attendance, calculates attendance averages, and flags students below the required 75% attendance threshold.

## 2. Learning Objectives
* Implement bulk database insertion and update routines.
* Secure endpoints so faculty members can only modify attendance for their assigned classes.
* Aggregate and process collections of transactional data using Java Streams.

## 3. Prerequisite Concepts
* Managing transactions using Spring Data JPA.
* Mapping database relationships (e.g., student enrollments to daily logs).
* Filtering and processing data collections.

## 4. Implementation Steps
1. Create the `Attendance` database entity class containing mappings for the enrollment reference, date, and status (e.g., PRESENT, ABSENT).
2. Build data access repositories for student enrollments and attendance logs.
3. Create the `AttendanceService` to handle core logic:
   * Fetching class lists for specific teaching sections.
   * Storing daily attendance grids in a single database transaction.
   * Calculating cumulative attendance percentages for students.
4. Create the `FacultyAttendanceController` to support faculty-facing operations, and `StudentAttendanceController` for students to view their attendance histories.

## 5. Files and Folders Created
* `src/main/java/com/ams/core/entity/Attendance.java` (Daily attendance log database entity)
* `src/main/java/com/ams/core/dto/BulkAttendanceDto.java` (Bulk update request payload DTO)
* `src/main/java/com/ams/core/repository/AttendanceRepository.java` (Data Repository)
* `src/main/java/com/ams/core/service/AttendanceService.java` (Business Logic Interface/Implementation)
* `src/main/java/com/ams/core/controller/FacultyAttendanceController.java` (Faculty-facing Controller)

## 6. Spring Boot Annotations Required
* `@Transactional` (Ensures atomic database writes; rolls back changes if any errors occur during bulk updates)
* `@PreAuthorize("hasRole('FACULTY')")` (Enforces access control constraints)
* `@Enumerated(EnumType.STRING)` (Maps attendance status enums to strings in the database)

## 7. Database Tables Involved
* `course_assignments` (Verifies class section ownership)
* `enrollments` (Validates student class registrations)
* `attendance` (Stores daily attendance logs)

## 8. API Endpoints Involved
* `POST /api/faculty/attendance` (Faculty records daily attendance; requires `ROLE_FACULTY`)
* `GET /api/faculty/attendance/class-list` (Faculty fetches roster for an assigned section; requires `ROLE_FACULTY`)
* `GET /api/students/attendance` (Student views their attendance summary; requires `ROLE_STUDENT`)

## 9. Common Mistakes
* **Executing Queries in Loops**: Querying the database inside loops during bulk updates, which can degrade database performance.
* **Missing Transaction Management**: Not marking bulk update service methods as `@Transactional`, which can result in partial database writes if an error occurs.
* **Insufficient Authorization Checks**: Allowing faculty members to record attendance for course sections they are not assigned to teach.

## 10. Interview Questions Related to this Module
* **Q**: What is the purpose of `@Transactional` in Spring Boot service methods, and how does it handle exceptions?
  * **A**: The `@Transactional` annotation defines the boundaries of a database transaction. It ensures that all database operations within the annotated method execute as a single, atomic unit. If a runtime exception occurs, Spring rolls back all modifications to ensure data consistency; if the method completes successfully, changes are committed.
* **Q**: How do you avoid executing database queries inside a loop when updating attendance for multiple students?
  * **A**: We can avoid executing queries inside loops by fetching the entire list of students in a single query (e.g., using `findAllById()` or matching course IDs), storing the results in a map for quick lookup, and then performing the bulk database update in a single transaction.

---

## 11. Learning-First Analysis

### Why It Exists
The attendance module helps track student participation and ensures compliance with academic policies. Real-time tracking helps identify students at risk of falling below academic thresholds.

### Java Concepts Used
* **Java Streams API**: Using streams to filter, map, and calculate attendance averages.
* **Enums**: Restricting attendance status values (e.g., PRESENT, ABSENT, EXCUSED) using Java enums.

### Spring Boot Concepts Used
* **Transaction Management**: Managing database transaction boundaries using `@Transactional`.
* **Method Security**: Enforcing role-based access control directly on service or controller methods.

### Connections to Other Modules
* Connects course assignments and student enrollments. Attendance records are linked to enrollments, which are managed within the course and student modules.

### Recruiter Evaluation Perspective
Recruiters evaluate how bulk updates are managed. They look for appropriate transaction management, optimized database queries (avoiding queries inside loops), and clean use of the Java Streams API for data analysis.
