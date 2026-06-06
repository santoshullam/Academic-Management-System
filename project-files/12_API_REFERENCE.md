# 12 API Reference - Academic Management System (AMS)

## 1. Purpose of the File
This file serves as the API reference for the system, documenting endpoints, HTTP methods, request payloads, response bodies, validation constraints, and role requirements.

## 2. Learning Objectives
* Design standardized, self-describing REST APIs.
* Enforce input validations using standard JSR 380 annotations.
* Map HTTP status codes (200, 201, 400, 401, 403, 404, 500) to API response states.

## 3. Prerequisite Concepts
* REST principles (statelessness, resource-oriented URIs, HTTP methods).
* HTTP status codes definition.
* JSON payload modeling.

## 4. Implementation Steps
1. Create a Postman environment config file to store host configurations.
2. Build mock requests inside Postman matching the specified endpoints.
3. Add JWT Bearer configurations in Postman for rapid testing.
4. Test and verify all endpoints to ensure they return standard JSON envelopes.

## 5. Files and Folders Created
* `project-files/12_API_REFERENCE.md` (This file)

## 6. Spring Boot Annotations Required
None (this is an API documentation reference).

## 7. Database Tables Involved
This covers all relational tables accessed by backend REST API endpoints.

## 8. API Endpoints Involved
All system REST endpoints.

## 9. Common Mistakes
* **Inconsistent URI Naming**: Mixing plural and singular terms or verbs in resource URIs (e.g., using `/api/getStudents` instead of `/api/students`). Use clean, noun-based, plural URIs.
* **Incorrect HTTP Methods**: Using `GET` requests to create resources instead of `POST`, or using `POST` to retrieve records.
* **Missing Error Formats**: Returning system stack traces during database exceptions instead of returning standardized JSON error envelopes.

## 10. Interview Questions Related to this Module
* **Q**: What makes an API RESTful, and how did you design your endpoints?
  * **A**: A RESTful API is resource-oriented, stateless, and uses standard HTTP methods to perform operations on resources. I designed my URIs around plural nouns representing resources (e.g., `/api/students`, `/api/courses`). I used `GET` to read, `POST` to create, `PUT` to update, and `DELETE` to remove resources, returning standard HTTP status codes and JSON envelopes.
* **Q**: How do you handle validation errors globally in a Spring Boot application?
  * **A**: I implemented a global exception handler using `@RestControllerAdvice`. When a controller validates a request body and validation fails (throwing a `MethodArgumentNotValidException`), the handler catches the exception, extracts the specific field errors, and formats them into a structured JSON response with a 400 Bad Request status code.

---

## 11. Endpoint Reference Matrix

### Authentication Endpoints
* **`POST /api/auth/login`**: Authenticates user credentials and issues a JWT token. Public access.
* **`POST /api/auth/refresh`**: Generates a new access token using a refresh token. Public access.

### Admin Endpoints
* **`POST /api/admin/students`**: Creates a student profile and security credentials. Requires `ROLE_ADMIN`.
* **`GET /api/admin/students`**: Retrieves a paginated list of students. Requires `ROLE_ADMIN`.
* **`POST /api/admin/faculty`**: Creates a faculty profile and security credentials. Requires `ROLE_ADMIN`.
* **`POST /api/courses`**: Creates a new course. Requires `ROLE_ADMIN`.
* **`POST /api/courses/assign`**: Assigns a course section to a faculty member. Requires `ROLE_ADMIN`.

### Faculty Endpoints
* **`GET /api/faculty/attendance/class-list`**: Fetches the class list for an assigned section. Requires `ROLE_FACULTY`.
* **`POST /api/faculty/attendance`**: Records bulk attendance for an assigned section. Requires `ROLE_FACULTY`.
* **`POST /api/faculty/marks`**: Uploads assessment marks for a student. Requires `ROLE_FACULTY`.

### Student Endpoints
* **`GET /api/students/profile`**: Fetches the logged-in student's profile. Requires `ROLE_STUDENT`.
* **`POST /api/students/enroll`**: Self-enrolls the student in a course section. Requires `ROLE_STUDENT`.
* **`GET /api/students/attendance`**: Fetches the student's attendance summary. Requires `ROLE_STUDENT`.
* **`GET /api/students/report-card`**: Fetches the student's academic transcript. Requires `ROLE_STUDENT`.

---

## 12. Learning-First Analysis

### Why It Exists
An API reference provides a clear contract between the frontend and the backend. It serves as documentation for developers and helps verify system integrations.

### Java Concepts Used
* **Records**: Using immutable Java records to define API request and response models.

### Spring Boot Concepts Used
* **Jackson JSON Mapper**: Automatically serializing Java objects to JSON and vice-versa.
* **Validation API**: Enforcing data validation constraints at the controller boundary.

### Connections to Other Modules
* Connects the frontend client application to all backend service modules.

### Recruiter Evaluation Perspective
Recruiters look for APIs designed according to REST standards. They check if resources are organized cleanly, if HTTP methods and status codes are used correctly, and if input validation is enforced consistently.
