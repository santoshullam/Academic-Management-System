# 03 Database Design - Academic Management System (AMS)

## 1. Purpose of the File
This file defines the database schema, normalized tables, primary/foreign keys, and constraints required for the Academic Management System MVP.

## 2. Learning Objectives
* Design a relational database schema normalized to 3NF.
* Configure database relationships (one-to-one, one-to-many, many-to-many) in a physical schema.
* Enforce integrity constraints (primary keys, unique indexes, and foreign keys).

## 3. Prerequisite Concepts
* SQL fundamentals (CREATE TABLE, foreign keys, constraints).
* Concept of normalization (1NF, 2NF, 3NF).
* Understanding of database joins and indexes.

## 4. Implementation Steps
1. Create a MySQL database instance named `academic_management_db`.
2. Write the DDL script to create the tables in order of dependency (independent tables like `departments` and `users` first, then dependent tables like `student_profiles` and `faculty_profiles`, and finally junction/transaction tables like `enrollments`, `attendance`, and `marks`).
3. Add unique constraints to prevent duplicate entries (e.g., matching student ID and course assignment ID in enrollments).
4. Apply index optimizations on frequently searched fields like `roll_number` and `employee_id`.

## 5. Files and Folders Created
* `src/main/resources/schema.sql` (Database table definitions)
* `src/main/resources/data.sql` (Initial seed data for departments, admin user, and sample courses)

## 6. Spring Boot Annotations Required
* `@Entity` (Declares JPA entity class)
* `@Table` (Specifies mapping database table name and unique constraints)
* `@Id` & `@GeneratedValue` (Sets auto-incrementing primary key)
* `@Column` (Specifies column properties, length, nullable status, and uniqueness)
* `@ManyToOne`, `@OneToMany`, `@OneToOne` (Defines association maps)
* `@JoinColumn` (Specifies physical foreign key column)

## 7. Database Tables Involved
* `users`
* `departments`
* `student_profiles`
* `faculty_profiles`
* `courses`
* `course_assignments`
* `enrollments`
* `attendance`
* `marks`

## 8. API Endpoints Involved
This database schema supports all API modules.

## 9. Common Mistakes
* **Cyclic Dependencies**: Designing entities in a way that creates recursive JSON serialization loop (cause of `InfiniteRecursion` exception). Fix by using `@JsonIgnore` or returning clean DTO records.
* **Missing Indexing on FKs**: Forgetting to index foreign keys, which can degrade database join query performance as data scales.
* **Orphan Records**: Not defining delete cascades on transaction tables, leaving orphaned entries when parent profiles are removed.

## 10. Interview Questions Related to this Module
* **Q**: What is the difference between `@OneToOne(fetch = FetchType.LAZY)` and `FetchType.EAGER`, and which one should be used for user profiles?
  * **A**: `FetchType.LAZY` loads the related entity only when it is accessed, while `FetchType.EAGER` loads it immediately with a database join. We should use `LAZY` for profile mappings to prevent loading unneeded details during standard login operations, which helps optimize application memory.
* **Q**: How do you implement a many-to-many relationship in a database schema, and why did we split it with a junction table entity?
  * **A**: A many-to-many relationship is implemented using a junction table containing foreign keys referencing both primary tables. We explicitly created the `Enrollment` entity as a junction table because we need to store additional attributes (e.g., enrollment status, final grades, and GPA calculations) on the relationship itself, which is not possible with a implicit `@ManyToMany` mapping.

---

## 11. Learning-First Analysis

### Why It Exists
A relational database provides structured, persistent, and transaction-safe data storage. Normalization ensures that academic records remain consistent and free of redundant entries.

### Java Concepts Used
* **Encapsulation**: Encapsulating data within entity fields and managing database state using object references.
* **Java Date and Time API**: Using `LocalDate` and `LocalDateTime` for consistent date tracking.

### Spring Boot Concepts Used
* **JPA & Hibernate (ORM)**: Translating Java object graphs into SQL tables, columns, and relations.
* **JPA Auditing**: Automatically capturing audit trails (`createdAt`, `updatedAt`) using `@EntityListeners`.

### Connections to Other Modules
* Serves as the persistence foundation. All other modules (Student, Faculty, Attendance, etc.) read from and write to this database schema.

### Recruiter Evaluation Perspective
Recruiters evaluate how database relationships are mapped in Java code. They look for appropriate fetch strategies (preferring `LAZY`), proper cascade configurations (preventing orphaned records), and well-designed unique constraints that maintain data integrity at the database engine level.
