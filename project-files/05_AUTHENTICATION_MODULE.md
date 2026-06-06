# 05 Authentication Module - Academic Management System (AMS)

## 1. Purpose of the File
This file guides the implementation of stateless token-based authorization using Spring Security and JWT, protecting API endpoints and setting up role-based access control.

## 2. Learning Objectives
* Secure REST APIs using stateless request interceptors.
* Understand the role of filters, security contexts, and user details in Spring Security.
* Configure JSON Web Token (JWT) signature signing, payload encryption, and verification.

## 3. Prerequisite Concepts
* Standard Java cryptography, keys, and algorithms.
* HTTP basic authentication vs. token-based authentication.
* Understanding of JWT structure (Header, Payload, Signature).

## 4. Implementation Steps
1. Add `jjwt` library dependencies to `pom.xml` to parse and sign JWT tokens.
2. Create `User` security entities mapping system login access accounts.
3. Implement `JwtUtils` to handle token generation and validation.
4. Implement `JwtAuthenticationFilter` to intercept requests, read the `Authorization` header, validate tokens, and authenticate users.
5. Configure `SecurityFilterChain` in `SecurityConfig` to restrict endpoints based on roles.
6. Build `AuthController` with `/api/auth/login` to authenticate credentials and issue tokens.

## 5. Files and Folders Created
* `src/main/java/com/ams/core/security/JwtUtils.java` (Token parser helper)
* `src/main/java/com/ams/core/security/JwtAuthenticationFilter.java` (Interceptor filter)
* `src/main/java/com/ams/core/security/CustomUserDetailsService.java` (User details loader)
* `src/main/java/com/ams/core/config/SecurityConfig.java` (Security filter configuration)
* `src/main/java/com/ams/core/controller/AuthController.java` (Authentication endpoint)

## 6. Spring Boot Annotations Required
* `@EnableWebSecurity` (Enables Spring Security web configurations)
* `@EnableMethodSecurity` (Enables method-level annotations like `@PreAuthorize`)
* `@Component` (Registers filters and utility classes as Spring beans)
* `@Bean` (Registers configuration beans)

## 7. Database Tables Involved
* `users` (Validates login credentials)

## 8. API Endpoints Involved
* `POST /api/auth/login` (Public login route)

## 9. Common Mistakes
* **Storing JWT in Thread-Unsafe Storage**: Storing user authentication status in static variables. Always use Spring's thread-local `SecurityContextHolder`.
* **Weak Secret Keys**: Using simple, short strings as JWT signature keys, which are vulnerable to brute-force attacks. Use a secure, 256-bit base64-encoded key.
* **Exposing Passwords in Responses**: Returning the database user entity (containing the hashed password) in the login response instead of using a clean DTO.

## 10. Interview Questions Related to this Module
* **Q**: What is the difference between Authentication and Authorization?
  * **A**: Authentication is the process of verifying who a user is (e.g., matching a username and password). Authorization is the process of verifying what they are allowed to do (e.g., checking if their role has permission to access an API).
* **Q**: Why do we use stateless sessions (JWT) instead of standard stateful sessions (JSESSIONID) in REST APIs?
  * **A**: Stateless sessions make applications easier to scale horizontally. Because the server does not store session state in memory, incoming requests can be routed to any available application instance without requiring session replication.

---

## 11. Learning-First Analysis

### Why It Exists
Security is critical in enterprise applications. The authentication module restricts system access, ensuring that sensitive data is only viewed or modified by authorized users.

### Java Concepts Used
* **Cryptography**: Using hashing algorithms (BCrypt) to store passwords securely.
* **Thread-Local Storage**: Spring Security uses thread-locals to store user contexts safely across concurrent requests.

### Spring Boot Concepts Used
* **Filter Chain Pattern**: Intercepting incoming requests using a chain of security filters.
* **Custom Security Configurations**: Overriding default configurations using `@Bean` definitions.

### Connections to Other Modules
* Secures all endpoints. Every functional module (Student, Faculty, Course, etc.) depends on the security context established by this module to authorize requests.

### Recruiter Evaluation Perspective
Recruiters evaluate how security filters are configured. They check if passwords are hashed using strong algorithms, if secrets are kept out of version control, and if role-based access is correctly enforced on APIs using method annotations.
