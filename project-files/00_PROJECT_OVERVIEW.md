# 00 Project Overview - Academic Management System (AMS)

## 1. Purpose of the File
This file establishes the foundational scope, learning path, and boundary lines for the MVP release of the Academic Management System (AMS). It acts as a reference document for overall project expectations.

## 2. Learning Objectives
* Understand how to define boundaries for a Minimum Viable Product (MVP).
* Map academic workflows (registration, grading, attendance) into software structures.
* Understand the role of users, access groups, and transactional security rules.

## 3. Prerequisite Concepts
* Basic understanding of HTTP and REST API concepts.
* Core programming concepts: variables, structures, and business logic.
* Relational database concepts (tables, relationships, queries).

## 4. Implementation Steps
1. Read this overview completely to align on what we are building.
2. Identify the target roles (`ADMIN`, `FACULTY`, `STUDENT`) and their corresponding user views.
3. Review the module-wise scope constraints to ensure no out-of-scope code is written.

## 5. Files and Folders Created
* `project-files/00_PROJECT_OVERVIEW.md` (This file)

## 6. Spring Boot Annotations Required
None (this is a project conceptual guide).

## 7. Database Tables Involved
* `users` (credentials tracking)
* `student_profiles`
* `faculty_profiles`

## 8. API Endpoints Involved
* `/api/auth/login` (Base login)

## 9. Common Mistakes
* **Scope Creep**: Attempting to add email notifications or database triggers in the initial 7-day phase. Keep it strictly MVP.
* **Loose Access Control**: Allowing students to accidentally view or modify pages meant for faculty.

## 10. Interview Questions Related to this Module
* **Q**: What is an MVP, and how did you decide the boundaries for this Academic Management System?
  * **A**: An MVP is the simplest version of a product that can be released to validate core workflows. For this system, I focused on three essential user journeys: Admins managing catalogs and profiles, Faculty recording attendance and grades, and Students viewing their transcripts and attendance levels. Features like bulk email reports and AWS autoscaling were deferred to later phases.

---

## 11. Learning-First Analysis

### Why It Exists
The project exists to provide a clear, real-world context for practicing Java 21, Spring Security, Hibernate, and REST API development. It models a complex, role-driven system that mirrors enterprise architectures.

### Java Concepts Used
* **Object-Oriented Design (OOD)**: Structuring system behaviors around entities (Student, Faculty, Course).
* **Encapsulation**: Enforcing strict state access restrictions inside security objects.

### Spring Boot Concepts Used
* **Stateless Session Management**: Configuring the backend to handle requests without storing user session states on the server.
* **Security Filter Chains**: Intercepting and routing API calls based on roles.

### Connections to Other Modules
* Integrates directly with all modules (Student, Faculty, Course, Attendance, Results) by defining their shared security constraints and data scopes.

### Recruiter Evaluation Perspective
Recruiters look for candidates who can take a large, complex problem, break it down into clean modular features, design a secure relational model, and implement it using professional conventions (e.g., proper status codes, clean code standards, and DTO layouts) instead of creating simple, unstructured scripts.
