# Academic Management System (AMS)

A full-stack, role-based Academic Management System built with Spring Boot and vanilla HTML/JS/CSS.

## Features

- **Role-Based Access Control (RBAC)**: Secure access with JWT for Admins, Faculty, and Students.
- **Admin Dashboard**: Manage users, faculty, students, courses, and department section assignments.
- **Faculty Dashboard**: Record daily student attendance and upload marks for assessments.
- **Student Dashboard**: Self-enroll in sections, view attendance records, and access an auto-calculated report card and CGPA.

## Tech Stack

- **Backend**: Java 21, Spring Boot 3.x, Spring Security, Spring Data JPA, JWT Authentication.
- **Database**: MySQL.
- **Frontend**: HTML5, Vanilla JavaScript, CSS3 (CSS Variables, Glassmorphism, Bootstrap 5 classes).
- **API Documentation**: OpenAPI / Swagger.

## Prerequisites

- Java 21+ installed
- MySQL Server running on `localhost:3306`

## Setup & Running Locally

1. **Configure the Database**
   Create a local MySQL database named `academic_management_db`:
   ```sql
   CREATE DATABASE academic_management_db;
   ```
   Ensure your MySQL credentials match what is in `src/main/resources/application.properties` (default is root/root).

2. **Run the Spring Boot Server**
   Use the Maven wrapper to build and start the application:
   ```bash
   .\mvnw clean spring-boot:run
   ```

3. **Access the Application**
   Once the server starts, navigate to:
   - **Main UI**: [http://localhost:8080](http://localhost:8080)
   - **API Documentation**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Default Users

The system is seeded with the following default users:

| Role      | Username      | Password             |
| --------- | ------------- | -------------------- |
| Admin     | `admin`       | `admin`              |
| Faculty   | `teacher_foo` | `TeacherPassword123` |
| Student   | `john_doe`    | `StudentPassword123` |

Enjoy using the Academic Management System!
