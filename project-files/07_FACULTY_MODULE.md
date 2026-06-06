# 07 Faculty Module - Academic Management System (AMS)

## 1. Purpose of the File
This file guides the implementation of the Faculty Management Module, which tracks faculty employee records, designations, department alignments, and class assignments.

## 2. Learning Objectives
* Design a modular data structure to store faculty employment metrics.
* Bind logins to employee profile tables using database-level constraints.
* Enforce security boundaries so faculty members can only view their assigned classes.

## 3. Prerequisite Concepts
* Managing database-level security and access controls.
* Mapping relationships between tables using Hibernate annotations.
* Writing custom database query logic.

## 4. Implementation Steps
1. Create the `FacultyProfile` database entity class.
2. Set up the `@OneToOne` association from `FacultyProfile` to the base user authentication entity.
3. Build the `FacultyService` to handle core business logic, such as looking up employee profiles and tracking teaching workloads.
4. Add `FacultyProfileRepository` with finder methods to lookup records by employee ID or department.
5. Create `AdminFacultyController` to support admin-facing CRUD operations, and `FacultyProfileController` for faculty members to view their profiles.

## 5. Files and Folders Created
* `src/main/java/com/ams/core/entity/FacultyProfile.java` (JPA Entity class)
* `src/main/java/com/ams/core/dto/FacultyRegistrationDto.java` (Registration DTO Record)
* `src/main/java/com/ams/core/repository/FacultyProfileRepository.java` (Repository interface)
* `src/main/java/com/ams/core/service/FacultyService.java` (Business Logic Interface/Implementation)
* `src/main/java/com/ams/core/controller/AdminFacultyController.java` (Admin-facing Controller)

## 6. Spring Boot Annotations Required
* `@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)` (Binds user login to employee profile)
* `@ManyToOne(fetch = FetchType.LAZY)` (Associates faculty with a department)
* `@PreAuthorize("hasRole('ADMIN')")` (Restricts controller methods to administrators)

## 7. Database Tables Involved
* `users` (Credentials tracking)
* `faculty_profiles` (Primary data table)
* `departments` (Binds faculty to their respective department)

## 8. API Endpoints Involved
* `POST /api/admin/faculty` (Admin registers a new faculty account; requires `ROLE_ADMIN`)
* `GET /api/admin/faculty` (Admin lists/filters faculty directory; requires `ROLE_ADMIN`)
* `GET /api/faculty/profile` (Faculty views their own profile details; requires `ROLE_FACULTY`)

## 9. Common Mistakes
* **Lack of Validation on Designations**: Accepting arbitrary, unvalidated strings for employee designations instead of restricting input to valid values.
* **Direct Entity Exposure**: Directly returning entity profiles containing password hashes in API responses. Use DTO records instead.
* **Cascading Delete Errors**: Deleting a faculty member's profile without updating or handling their active teaching assignments.

## 10. Interview Questions Related to this Module
* **Q**: What is the difference between constructor injection and field injection (`@Autowired` on fields), and why is constructor injection preferred?
  * **A**: Field injection uses reflection to inject dependencies directly into fields, which makes classes harder to unit test without starting a full Spring container. Constructor injection is preferred because it makes dependencies explicit, supports the use of final fields, and allows creating instances using standard constructors in unit tests.
* **Q**: How do you prevent recursive mapping issues when mapping a bidirectional one-to-many relationship in Hibernate?
  * **A**: Bidirectional relationships can cause infinite loops during JSON serialization (e.g., Department references Faculty, which references Department). We can prevent this by using DTO projections to return only required fields, or by using annotations like `@JsonManagedReference` and `@JsonBackReference` on entity classes.

---

## 11. Learning-First Analysis

### Why It Exists
The faculty module is a core part of the system. It tracks teaching assignments, workloads, and department mappings, which are required for student enrollments, attendance, and grading.

### Java Concepts Used
* **Encapsulation**: Restricting direct access to class fields using private access modifiers and getter/setter methods.
* **Inheritance**: Inheriting shared auditing properties from the `BaseEntity` parent class.

### Spring Boot Concepts Used
* **Constructor Injection**: Injecting repositories and services using class constructors.
* **Stereotype Beans**: Using annotations like `@Service` and `@Repository` to register classes as managed Spring beans.

### Connections to Other Modules
* Connects base login credentials to faculty profiles. Faculty profiles are referenced in course assignments, attendance sheets, and grade records.

### Recruiter Evaluation Perspective
Recruiters evaluate how the one-to-one relationship is mapped. They look for clean separation between user security credentials and business profiles, appropriate cascade configurations, and consistent input validation at the API boundary.
