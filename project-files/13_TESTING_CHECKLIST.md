# 13 Testing Checklist - Academic Management System (AMS)

## 1. Purpose of the File
This file provides a testing checklist to verify backend REST APIs and frontend flows using Postman and JUnit unit tests.

## 2. Learning Objectives
* Perform integration testing on REST APIs using Postman.
* Write unit tests for services using Mockito to mock repository layers.
* Verify security configurations and access controls using test suites.

## 3. Prerequisite Concepts
* Unit testing vs. Integration testing.
* Using Mockito to mock dependencies in Java.
* Mocking HTTP requests using Spring MockMvc.

## 4. Implementation Steps
1. Create a JUnit 5 test class for `StudentService` to verify profile creation and validation rules.
2. Write test cases to verify that unauthorized requests are rejected by Spring Security.
3. Use Mockito (`@Mock`, `@InjectMocks`) to mock database dependencies in service tests.
4. Create a Postman collection containing tests to verify all system endpoints.

## 5. Files and Folders Created
* `src/test/java/com/ams/core/service/StudentServiceTest.java` (JUnit service unit tests)
* `src/test/java/com/ams/core/controller/AuthControllerTest.java` (MockMvc web controllers tests)
* `project-files/AMS_Postman_Collection.json` (Exported Postman test collections)

## 6. Spring Boot Annotations Required
* `@SpringBootTest` (Loads the application context for integration tests)
* `@ExtendWith(MockitoExtension.class)` (Enables Mockito annotations in JUnit tests)
* `@AutoConfigureMockMvc` (Configures MockMvc for controller testing)

## 7. Database Tables Involved
Testing interacts with all database tables using mock data or an in-memory database (e.g., H2).

## 8. API Endpoints Involved
All system API endpoints.

## 9. Common Mistakes
* **Coupling Tests to the Database**: Running unit tests against a live database instead of mocking the database layer. This can cause tests to fail due to data differences or connection issues.
* **Skipping Security Boundary Tests**: Only testing successful request paths and neglecting to test that unauthorized requests are blocked.
* **Lack of Assertions**: Writing tests that only run code without asserting the expected outputs or states.

## 10. Interview Questions Related to this Module
* **Q**: What is the difference between `@Mock` and `@MockBean` in Spring Boot tests?
  * **A**: `@Mock` is a Mockito annotation that creates a mock instance of a class without loading the Spring container, which is ideal for fast unit tests. `@MockBean` is a Spring Boot annotation that creates a mock instance and registers it as a bean in the Spring application context, which is useful for integration tests.
* **Q**: Why do we mock database queries in unit tests instead of running tests against a live database?
  * **A**: Mocking database queries isolates the service logic from the persistence layer. This ensures that tests run quickly, do not modify database state, and do not fail due to external database connection issues or schema differences.

---

## 11. Testing Checklist Matrix

### Core REST API Tests
* [ ] Verify that `/api/auth/login` returns a valid JWT token.
* [ ] Verify that access is blocked when requesting protected endpoints without a token.
* [ ] Verify that students cannot access admin-only endpoints.
* [ ] Verify that registration fails if the email format is invalid.

### Service Logic Tests
* [ ] Verify that unique student roll numbers are generated correctly.
* [ ] Verify that course credits are validated correctly.
* [ ] Verify that GPA calculations return the correct values for completed courses.
* [ ] Verify that attendance warning flags are triggered correctly.

---

## 12. Learning-First Analysis

### Why It Exists
Testing verifies that the application works correctly under different scenarios. Automated unit and integration tests help prevent regressions and ensure code reliability as the codebase evolves.

### Java Concepts Used
* **Mockito API**: Mocking class dependencies to isolate code blocks under test.
* **Assertions Framework**: Verifying test outcomes using `assertEquals`, `assertNotNull`, and `assertThrows`.

### Spring Boot Concepts Used
* **MockMvc**: Mocking HTTP request and response lifecycles without starting a full web server.
* **Test Configurations**: Setting up database profiles (e.g., using H2) for integration tests.

### Connections to Other Modules
* Validates the implementation and logic of all functional modules.

### Recruiter Evaluation Perspective
Recruiters look for projects that include structured testing. They check if unit tests cover edge cases, if mock objects are configured correctly, and if tests verify security access controls at the controller boundary.
