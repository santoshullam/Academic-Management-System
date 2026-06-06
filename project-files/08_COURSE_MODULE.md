# 08 Course Module - Academic Management System (AMS)

## 1. Purpose of the File
This file guides the implementation of the Course Management Module, which handles course catalogs, department configurations, and teaching section assignments.

## 2. Learning Objectives
* Map hierarchical academic courses and associate them with departments.
* Design and implement relational schemas for teaching assignments.
* Restrict course configurations and faculty assignments to system administrators.

## 3. Prerequisite Concepts
* Managing database relations using Hibernate annotations.
* Validating input data using spring validation features.
* Implementing role-based endpoint access control.

## 4. Implementation Steps
1. Create database entities for `Course` and `CourseAssignment`.
2. Build data access repositories for departments, courses, and course assignments.
3. Implement a service layer to validate department assignments and course credit limits (e.g., credit values between 1 and 5).
4. Create an endpoint allowing admins to assign faculty members to courses for specific semesters (e.g., Fall 2026).
5. Build an endpoint allowing students to search for courses offered by their department.

## 5. Files and Folders Created
* `src/main/java/com/ams/core/entity/Course.java` (Course catalog database entity)
* `src/main/java/com/ams/core/entity/CourseAssignment.java` (Teaching assignment database entity)
* `src/main/java/com/ams/core/dto/CourseCreationDto.java` (Request DTO Record)
* `src/main/java/com/ams/core/repository/CourseRepository.java` (Data Repository)
* `src/main/java/com/ams/core/service/CourseService.java` (Business Logic Interface/Implementation)
* `src/main/java/com/ams/core/controller/CourseController.java` (REST Endpoint Router)

## 6. Spring Boot Annotations Required
* `@ManyToOne(fetch = FetchType.LAZY)` (Links courses to departments)
* `@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)` (Cascade assignments configuration)
* `@PreAuthorize("hasRole('ADMIN')")` (Enforces access control constraints)

## 7. Database Tables Involved
* `departments` (Associates courses with academic departments)
* `courses` (Stores catalog information)
* `faculty_profiles` (Associates faculty with teaching sections)
* `course_assignments` (Junction table linking course sections to faculty)

## 8. API Endpoints Involved
* `POST /api/courses` (Admin creates a new course; requires `ROLE_ADMIN`)
* `POST /api/courses/assign` (Admin assigns a course section to a faculty member; requires `ROLE_ADMIN`)
* `GET /api/courses/catalog` (Public/Student directory listing to view available courses)

## 9. Common Mistakes
* **Duplicate Assignments**: Allowing the same course section to be assigned to the same faculty member multiple times. Prevent this using database-level unique constraints.
* **Eager Loading on Large Tables**: Fetching all course details eagerly, which can trigger unnecessary database queries.
* **Missing Credential Validation**: Allowing course configurations with invalid credit values (e.g., negative credits or credits exceeding maximum limits).

## 10. Interview Questions Related to this Module
* **Q**: What is the difference between `@ManyToOne` and `@ManyToMany` relationships, and how is each mapped in JPA?
  * **A**: A `@ManyToOne` relationship indicates that multiple entities can relate to a single parent entity (e.g., multiple courses belong to one department), which is mapped using a foreign key in the child table. A `@ManyToMany` relationship indicates that multiple records on both sides relate to each other, which requires an intermediate junction table to map the associations.
* **Q**: What is the N+1 query problem, and how do you resolve it when querying a course catalog?
  * **A**: The N+1 query problem occurs when Hibernate executes one query to fetch a list of entities (e.g., all courses), and then executes separate queries for each record to load its lazy relationships (e.g., fetching the department details for each course). We can resolve this using `JOIN FETCH` (e.g., `SELECT c FROM Course c JOIN FETCH c.department`) in our repository query to load related entities in a single database join.

---

## 11. Learning-First Analysis

### Why It Exists
The course module is a core part of the system. It tracks course details, credit structures, and teaching assignments, which are required for student enrollments, attendance, and grading.

### Java Concepts Used
* **Encapsulation**: Encapsulating course details and attributes within private fields.
* **Collections Framework**: Storing and managing course assignments using standard lists.

### Spring Boot Concepts Used
* **Declarative Security**: Restricting course configurations and updates to admin roles using security annotations.
* **Query Customizations**: Writing custom SQL queries using Spring Data JPA.

### Connections to Other Modules
* Connects departments to courses. Courses are referenced in teaching assignments, student enrollments, attendance sheets, and academic grade reports.

### Recruiter Evaluation Perspective
Recruiters evaluate how database relationships are mapped. They look for clean relational models, optimized database queries, and proper fetch configurations to prevent performance issues like the N+1 query problem.
