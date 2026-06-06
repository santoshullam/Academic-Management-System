# 06 Student Module - Academic Management System (AMS)

## 1. Purpose of the File
This file guides the implementation of the Student Management Module, which handles creating, reading, updating, and deleting student profile records and linking them to base security user accounts.

## 2. Learning Objectives
* Model one-to-one relationships between authentication entities and business profile entities.
* Implement server-side DTO validations using the JSR 380 framework.
* Design paginated and searchable directory listings for student records.

## 3. Prerequisite Concepts
* JPA cascade types (`ALL`, `PERSIST`, `REMOVE`) and how they affect related entities.
* JPA data fetch strategies (Lazy vs Eager loading).
* Designing RESTful APIs for resource creation.

## 4. Implementation Steps
1. Create the `StudentProfile` database entity class extending the auditable base class.
2. Define the `@OneToOne` mapping from `StudentProfile` to the core security `User` entity.
3. Write `StudentRegistrationDto` and `StudentResponseDto` payload records.
4. Code `StudentProfileRepository` with custom finders (e.g., search by `rollNumber` or department).
5. Build `StudentService` containing business rules (like auto-generating unique roll numbers).
6. Create `AdminStudentController` to expose CRUD endpoints for administrators, and `StudentProfileController` for students to access their own records.

## 5. Files and Folders Created
* `src/main/java/com/ams/core/entity/StudentProfile.java` (JPA Database Entity)
* `src/main/java/com/ams/core/dto/StudentRegistrationDto.java` (Request DTO Record)
* `src/main/java/com/ams/core/dto/StudentResponseDto.java` (Response DTO Record)
* `src/main/java/com/ams/core/repository/StudentProfileRepository.java` (Data Repository)
* `src/main/java/com/ams/core/service/StudentService.java` (Business Logic Interface/Implementation)
* `src/main/java/com/ams/core/controller/AdminStudentController.java` (Admin-facing Controller)

## 6. Spring Boot Annotations Required
* `@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)` (Establishes lazy profile binding)
* `@JoinColumn` (Binds physical database foreign keys)
* `@Valid` (Triggers validation constraints on request bodies)
* `@PreAuthorize("hasRole('ADMIN')")` (Restricts controller methods to administrators)

## 7. Database Tables Involved
* `users` (Managed via profile cascades)
* `student_profiles` (Primary data table)
* `departments` (Resolves student academic department mappings)

## 8. API Endpoints Involved
* `POST /api/admin/students` (Admin registers student; requires `ROLE_ADMIN`)
* `GET /api/admin/students` (Admin lists/filters student directory; requires `ROLE_ADMIN`)
* `GET /api/students/profile` (Student views their own profile details; requires `ROLE_STUDENT`)
* `PUT /api/students/profile` (Student updates contact info; requires `ROLE_STUDENT`)

## 9. Common Mistakes
* **Cascade Delete Errors**: Deleting a student profile but leaving the parent user login record in the database. Use `CascadeType.ALL` to clean up related records automatically.
* **Direct Entity Exposure**: Directly returning the `StudentProfile` entity in API responses, which can leak internal password hashes or trigger lazy-loading exceptions.
* **Missing Uniqueness Constraints**: Forgetting to set `roll_number` as unique in both the database schema and JPA mappings, which can result in duplicate identifiers.

## 10. Interview Questions Related to this Module
* **Q**: What is the difference between `@OneToOne` and `@ManyToOne` relationships, and when should each be used?
  * **A**: `@OneToOne` indicates that each record in the primary table maps to exactly one record in the related table (e.g., a student has exactly one user login). `@ManyToOne` indicates that multiple records in the primary table can map to a single record in the related table (e.g., multiple students belong to the same academic department).
* **Q**: How does Hibernate handle updates on entity fields that are mapped with `@OneToOne(cascade = CascadeType.ALL)`?
  * **A**: When `CascadeType.ALL` is set, changes made to the child profile entity (e.g., updates to name or phone number) are automatically propagated to the parent entity and saved to the database when the transaction commits.

---

## 11. Learning-First Analysis

### Why It Exists
The student module is a core part of the system. It tracks student profile data, contact details, and departmental mappings, which are required for enrollments, attendance, and grading.

### Java Concepts Used
* **Encapsulation**: Using getter/setter methods to protect and manage student profile fields.
* **Records**: Using Java records to define immutable data structures (DTOs) for API payloads.

### Spring Boot Concepts Used
* **Declarative Validation**: Using validation constraints like `@Email`, `@NotBlank`, and `@Size` to validate request payloads.
* **Repository Projections**: Retrieving specific fields from the database to improve query efficiency.

### Connections to Other Modules
* Connects base login credentials to student business profiles. Student profiles are referenced in enrollments, attendance records, and grade reports.

### Recruiter Evaluation Perspective
Recruiters evaluate how the one-to-one relationship is mapped. They look for clean separation between user security credentials and business profiles, appropriate cascade configurations, and consistent input validation at the API boundary.
