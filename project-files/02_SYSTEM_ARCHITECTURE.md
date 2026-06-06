# 02 System Architecture - Academic Management System (AMS)

## 1. Purpose of the File
This file explains the architectural layout of the Academic Management System, detail the flow of requests from the browser to the database, and define the responsibilities of each architectural tier.

## 2. Learning Objectives
* Understand the Layered Architecture Pattern (Controller, Service, Repository, DTO).
* Trace the Request-Response lifecycle across security filters and controllers.
* Understand the division of responsibilities between layers.

## 3. Prerequisite Concepts
* Client-Server communication model.
* Basics of HTTP requests, status codes, and JSON serialization.
* Basic understanding of Spring dependency injection and IoC containers.

## 4. Implementation Steps
1. Study the layer responsibility matrix.
2. Design data structures that flow between layers (DTOs vs. Entities).
3. Ensure no database entity is directly returned from the Controller layer (enforce DTO conversion).
4. Implement a centralized Global Exception Handler to catch and process exceptions from all layers.

## 5. Files and Folders Created
* `project-files/02_SYSTEM_ARCHITECTURE.md` (This file)

## 6. Spring Boot Annotations Required
* `@RestController` (Marks controller classes)
* `@Service` (Marks service beans)
* `@Repository` (Marks data repositories)
* `@Autowired` or Constructor Injection (Performs DI)
* `@RestControllerAdvice` (Defines global exception interceptors)

## 7. Database Tables Involved
This covers how all relational tables map to Entities processed across application layers.

## 8. API Endpoints Involved
All HTTP request routes mapping to Controller methods.

## 9. Common Mistakes
* **Leaking Database Entities**: Directly returning JPA Entities from controllers. This can lead to security risks and lazy initialization errors.
* **Putting Business Logic in Controllers**: Writing calculations or database calls directly in the controller class. Controllers should only handle request routing and validation.
* **Direct Database Dependency**: Letting controllers access repositories directly, bypassing the service layer.

## 10. Interview Questions Related to this Module
* **Q**: What are the benefits of a Layered Architecture, and why shouldn't controllers access repositories directly?
  * **A**: Layered architecture enforces separation of concerns, which makes the codebase easier to test and maintain. By routing database requests through the service layer, we can manage transaction boundaries, apply business validation rules, and combine multiple database calls into a single unit of work.
* **Q**: What is the N+1 select problem in JPA, and how does it relate to system architecture?
  * **A**: The N+1 select problem occurs when a query fetches a parent entity, and then executes separate queries for each child relationship. In a layered system, if lazy loading is triggered outside of a transaction (e.g., during DTO conversion in the controller), it can cause performance issues or throw a `LazyInitializationException`. We can prevent this by using `JOIN FETCH` queries or `@EntityGraph` in the repository layer.

---

## 11. Learning-First Analysis

### Why It Exists
Layered architecture organizes code based on responsibility. This prevents changes in the database or business logic from breaking the user interface, and vice-versa.

### Java Concepts Used
* **Abstraction**: Defining service interfaces (e.g., `StudentService`) so the controller does not depend on a concrete implementation.
* **Dependency Injection (DI)**: Letting the Spring framework handle object creation and lifecycle management.

### Spring Boot Concepts Used
* **IoC Container**: Manages Spring beans and injects them where needed.
* **Stereotype Annotations**: `@RestController`, `@Service`, and `@Repository` tell Spring how to configure and manage each class.

### Connections to Other Modules
* Connects the user interface directly to the database. All functional modules (Student, Faculty, Attendance, etc.) use this layered structure.

### Recruiter Evaluation Perspective
Recruiters look for projects that show a clear separation of concerns. They check whether business logic is isolated within the service layer, whether inputs are validated at the controller boundary, and whether database entities are kept separate from public API models using DTOs.
