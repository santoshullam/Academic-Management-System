# 10 Result Module - Academic Management System (AMS)

## 1. Purpose of the File
This file guides the implementation of the Result and Grading Module, which calculates student grades based on weighted assessments and computes GPA and CGPA metrics.

## 2. Learning Objectives
* Design and implement grade calculation algorithms.
* Perform floating-point math and formatting operations securely.
* Aggregate and summarize student grades using database queries.

## 3. Prerequisite Concepts
* GPA/CGPA calculation models (e.g., mapping percentages to letter grades and grade points).
* Implementing custom JPQL queries and database joins.
* Basic understanding of transaction management.

## 4. Implementation Steps
1. Create the `Mark` database entity class containing mappings for the enrollment reference, assessment name, marks obtained, maximum marks, and weightage percentage.
2. Build data access repositories for student marks.
3. Create the `GradeCalculationService` to handle core logic:
   * Fetching and saving marks for specific student enrollments.
   * Calculating cumulative weighted scores and letter grades (e.g., A, B, C).
   * Calculating GPA and CGPA values based on completed courses.
4. Create the `FacultyMarksController` to support faculty-facing operations, and `StudentResultController` for students to view their grade reports and transcripts.

## 5. Files and Folders Created
* `src/main/java/com/ams/core/entity/Mark.java` (Marks database entity class)
* `src/main/java/com/ams/core/dto/UploadMarkDto.java` (Request DTO Record)
* `src/main/java/com/ams/core/dto/ReportCardDto.java` (Response DTO Record)
* `src/main/java/com/ams/core/repository/MarkRepository.java` (Data Repository)
* `src/main/java/com/ams/core/service/ResultService.java` (Business Logic Interface/Implementation)
* `src/main/java/com/ams/core/controller/FacultyMarksController.java` (Faculty-facing Controller)

## 6. Spring Boot Annotations Required
* `@Transactional` (Ensures database write consistency)
* `@PreAuthorize("hasRole('FACULTY')")` (Restricts grade entry and modifications to faculty)
* `@Query` (Executes custom JPQL aggregation queries)

## 7. Database Tables Involved
* `enrollments` (Binds results to student registrations)
* `marks` (Stores detailed assessment scores)
* `courses` (Provides course credits for GPA calculations)

## 8. API Endpoints Involved
* `POST /api/faculty/marks` (Faculty uploads marks for an enrollment; requires `ROLE_FACULTY`)
* `GET /api/students/report-card` (Student fetches their transcript; requires `ROLE_STUDENT`)

## 9. Common Mistakes
* **Exceeding Maximum Marks**: Not validating that the marks obtained are less than or equal to the maximum possible marks.
* **Exceeding Total Weightage**: Allowing the sum of assessment weightages for a course to exceed 100%, which can lead to incorrect GPA calculations.
* **Inaccurate Calculations**: Using floating-point variables (like `double` or `float`) for financial or critical academic calculations instead of using `BigDecimal` where precision is required.

## 10. Interview Questions Related to this Module
* **Q**: How did you handle GPA and CGPA calculations in the database? Did you use database views, triggers, or compute them in the Java service layer?
  * **A**: I implemented the GPA and CGPA calculation engine in the Java service layer to keep business logic separate from the database schema. The service layer fetches a student's active enrollments and course credits, calculates their weighted grades, and computes the GPA. This ensures that calculations remain testable and independent of the database vendor.
* **Q**: What is the difference between `@Transactional(readOnly = true)` and standard `@Transactional`?
  * **A**: `@Transactional(readOnly = true)` optimizes database performance for read-only operations. It tells Hibernate to bypass dirty-checking mechanisms and database locks, which reduces overhead and improves query execution speeds.

---

## 11. Learning-First Analysis

### Why It Exists
The result module handles grading and GPA tracking. Automating these calculations ensures accuracy, reduces administrative errors, and gives students immediate access to their academic transcripts.

### Java Concepts Used
* **Java Streams API**: Using streams to map percentage scores to letter grades and calculate GPA averages.
* **Math Operations**: Performing precise calculations for weighted scores and grades.

### Spring Boot Concepts Used
* **Custom Aggregations**: Writing custom database queries using `@Query`.
* **Method Security**: Enforcing role-based access control directly on REST controllers.

### Connections to Other Modules
* Connects course details and student enrollments. Marks are linked to enrollments, which reference courses and student profiles managed in other modules.

### Recruiter Evaluation Perspective
Recruiters evaluate the design of the grade calculation logic. They look for clean math implementations, proper transaction management, and optimized database queries that prevent performance issues when generating student transcripts.
