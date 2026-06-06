# 01 Weekly Roadmap - Academic Management System (AMS)

## 1. Purpose of the File
This file outlines the day-by-day learning roadmap, development tasks, hours breakdown, and commit expectations to complete the Academic Management System MVP within a realistic 7-day timeframe.

## 2. Learning Objectives
* Structure project milestones to enforce progress.
* Plan atomic Git commits linked to specific features.
* Balance research, design, coding, testing, and documentation tasks.

## 3. Prerequisite Concepts
* Standard Git commands (`git add`, `git commit`, `git checkout -b`).
* Basic time management and task breakdown skills.
* Maven build process basics.

## 4. Implementation Steps
1. Review the daily tasks at the start of each development day.
2. Read the "Concepts to Learn" for the day before writing code.
3. Write clean code matching the day's deliverables.
4. Test and verify features.
5. Make atomic commits conforming to the conventional commit style.

## 5. Files and Folders Created
* `project-files/01_WEEKLY_ROADMAP.md` (This file)

## 6. Spring Boot Annotations Required
None (this is a project roadmap guide).

## 7. Database Tables Involved
All system tables (designed across Day 1 - Day 5).

## 8. API Endpoints Involved
All system endpoints (implemented across Day 1 - Day 6).

## 9. Common Mistakes
* **Lack of Testing**: Delaying testing until Day 6. You should test endpoints as soon as they are completed using Postman.
* **Giant Commits**: Committing a whole day's work in one single commit. Make small, focused commits (e.g., separate database entities, security configurations, and business logic changes).

## 10. Interview Questions Related to this Module
* **Q**: How do you prioritize tasks under tight deadlines when developing an application?
  * **A**: I prioritize the database schema and security core first, since business workflows depend on them. Next, I implement features according to user hierarchies (Admins, then Faculty, then Students). Finally, I focus on UI integration and polish.

---

## 11. 7-Day Day-by-Day Execution Plan

### Day 1: Project Setup, Database Design, & Spring Boot Configuration
* **Goals**: Configure the core environment, design a normalized schema, and establish database connectivity.
* **Concepts to Learn**: Spring Initializr, JPA configurations, MySQL configuration, and Entity Mapping relationships.
* **Tasks to Complete**:
  1. Initialize the project using Spring Initializr.
  2. Write entity mappings (`User`, `Department`, `StudentProfile`, `FacultyProfile`, `Course`, `CourseAssignment`, `Enrollment`, `Attendance`, `Mark`).
  3. Configure `application.yml` for database connections and Hibernate dialect.
* **Deliverables**: Compile-ready Spring Boot project skeleton connected to a local MySQL instance.
* **Expected Git Commits**:
  * `feat(init): initialize Spring Boot Maven skeleton`
  * `feat(db): implement entities and relationships`
* **Estimated Hours**: 6 hours

---

### Day 2: Authentication Module, JWT Setup, & Security Configuration
* **Goals**: Secure the application with a stateless, token-based authentication mechanism.
* **Concepts to Learn**: Spring Security filter chains, stateless sessions, JWT validation, and Password Hashing (BCrypt).
* **Tasks to Complete**:
  1. Implement Spring Security Configurations.
  2. Implement `JwtUtils`, `JwtAuthenticationFilter`, and `CustomUserDetailsService`.
  3. Add `/api/auth/login` endpoint to return JWTs.
* **Deliverables**: Secure REST APIs rejecting requests lacking a valid Bearer token.
* **Expected Git Commits**:
  * `feat(security): configure Spring Security filter chain`
  * `feat(auth): implement JWT generation and authentication filter`
* **Estimated Hours**: 8 hours

---

### Day 3: Student Management Module
* **Goals**: Build student profile data services and registration systems.
* **Concepts to Learn**: `@OneToOne` mapping strategies, JSR 380 validations, DTO projection, and Pagination.
* **Tasks to Complete**:
  1. Code `StudentProfileRepository` and `StudentService`.
  2. Write `AdminStudentController` containing endpoints to register, view, update, and search students.
  3. Write request validation rules.
* **Deliverables**: Secure endpoints allowing admins to register students and students to view profiles.
* **Expected Git Commits**:
  * `feat(student): build student profile repository and service`
  * `feat(student): build admin-facing student registration REST API`
* **Estimated Hours**: 6 hours

---

### Day 4: Faculty & Course Modules
* **Goals**: Implement teaching assignments, department configurations, and course registry features.
* **Concepts to Learn**: Cascade types, `@ManyToOne` vs `@ManyToMany` relationships, and custom JPQL queries.
* **Tasks to Complete**:
  1. Add Department, Faculty, and Course CRUD services.
  2. Implement Course Assignment endpoint allowing admins to assign faculty to sections.
  3. Write validations verifying department IDs and course credits.
* **Deliverables**: Course registry APIs and Class roster generation APIs.
* **Expected Git Commits**:
  * `feat(course): implement department and course catalog APIs`
  * `feat(faculty): build faculty profiles and course assignments`
* **Estimated Hours**: 7 hours

---

### Day 5: Attendance & Results Modules
* **Goals**: Implement daily attendance logging and academic mark recording with automatic calculations.
* **Concepts to Learn**: Transaction Management (`@Transactional`), bulk operations, Java Streams calculations, and database connection pooling metrics.
* **Tasks to Complete**:
  1. Implement bulk attendance log endpoints for faculty.
  2. Implement mark records and weighted score calculator engines.
  3. Build student grade reports and GPA/CGPA calculators.
* **Deliverables**: Working business layer tracking attendance averages and GPAs.
* **Expected Git Commits**:
  * `feat(attendance): build bulk attendance recording API`
  * `feat(results): implement marks uploader and GPA calculation engine`
* **Estimated Hours**: 8 hours

---

### Day 6: Frontend Integration, API Testing, & Bug Fixing
* **Goals**: Integrate the user interface and verify system endpoints.
* **Concepts to Learn**: AJAX/Fetch APIs, token storage in headers, Bootstrap DOM manipulation, and CORS policies.
* **Tasks to Complete**:
  1. Build login, student, faculty, and admin dashboards using Bootstrap.
  2. Write JavaScript routines storing JWT tokens and intercepting status errors.
  3. Resolve CORS issues in Spring Security configurations.
* **Deliverables**: Connected frontend dynamically rendering records from API responses.
* **Expected Git Commits**:
  * `feat(ui): design login and dashboard layouts`
  * `feat(ui): connect JavaScript fetch to backend API`
* **Estimated Hours**: 8 hours

---

### Day 7: Refactoring, Documentation, GitHub Setup, & Resume Preparation
* **Goals**: Format code, write documentation, publish code, and review interview points.
* **Concepts to Learn**: Clean Code standards, Git branching styles, resume description formats, and mock interview preparations.
* **Tasks to Complete**:
  1. Clean up unused imports, format code, and optimize queries using JPQL joins.
  2. Write the repository `README.md`.
  3. Publish code on GitHub.
  4. Review typical database design and Spring Security interview questions.
* **Deliverables**: Clean production-ready codebase, complete README, and resume bullets.
* **Expected Git Commits**:
  * `refactor(core): optimize JPA queries and format code`
  * `docs(readme): create project readme documentation`
* **Estimated Hours**: 5 hours

---

## 12. Learning-First Analysis

### Why It Exists
A timeline structure keeps development focused and helps track key milestones. It ensures that complex modules are built in a logical order, preventing delays and integration issues.

### Java Concepts Used
* **Task breakdown**: Decomposing a large system into smaller, manageable daily tasks.

### Spring Boot Concepts Used
* **Feature Flags & Modular configurations**: Planning how configuration changes are layered over time.

### Connections to Other Modules
* Governs the timing and implementation sequence of all modules in the project.

### Recruiter Evaluation Perspective
Recruiters appreciate candidates who can manage their time effectively, plan their commits logically, and follow a structured roadmap, as these are critical skills for collaborating on professional development teams.
