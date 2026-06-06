# 04 Backend Setup - Academic Management System (AMS)

## 1. Purpose of the File
This file guides the initial environment creation, Maven configurations, dependency setups, and properties configurations for the Spring Boot backend.

## 2. Learning Objectives
* Bootstrap a Spring Boot project using Maven.
* Understand compile-time vs. runtime dependencies in `pom.xml`.
* Configure application environment parameters in YAML profiles.

## 3. Prerequisite Concepts
* Java JDK installation (version 21).
* Basics of Maven lifecycle commands (`clean`, `install`, `compile`).
* Structure of properties/YAML configurations.

## 4. Implementation Steps
1. Navigate to Spring Initializr (or use IntelliJ) and create a project with:
   * **Project**: Maven Project
   * **Language**: Java
   * **Spring Boot**: 3.2.x (or latest stable)
   * **Java Version**: 21
   * **Dependencies**: Spring Web, Spring Security, Spring Data JPA, MySQL Driver, Lombok, Validation.
2. Extract the project skeleton into the workspace folder.
3. Set up the `application.yml` file in `src/main/resources/`.
4. Run `mvn clean compile` to download dependencies and verify the setup.

## 5. Files and Folders Created
* `pom.xml` (Maven build file)
* `src/main/resources/application.yml` (App configuration)
* Directory tree: `config`, `controller`, `dto`, `entity`, `exception`, `repository`, `security`, `service`, `util` under `src/main/java/com/ams/core/`

## 6. Spring Boot Annotations Required
* `@SpringBootApplication` (Bootstraps the Spring context)
* `@EnableJpaAuditing` (Enables audit tracking)

## 7. Database Tables Involved
None directly (configuration only).

## 8. API Endpoints Involved
None.

## 9. Common Mistakes
* **Incorrect Java Version**: Choosing Java 17 or 11 in Spring Initializr but having Java 21 configured in your IDE, which causes build errors.
* **Typo in YAML Indentation**: YAML is space-sensitive. Using tabs instead of spaces will fail to parse and prevent the application from starting.
* **Missing MySQL Driver**: Forgetting to add the MySQL database connector dependency in `pom.xml`, which leads to database driver initialization failures.

## 10. Interview Questions Related to this Module
* **Q**: What does `@SpringBootApplication` do under the hood?
  * **A**: It is a convenience annotation that combines three annotations: `@Configuration` (allows registering extra beans), `@EnableAutoConfiguration` (tells Spring Boot to automatically configure beans based on class path settings), and `@ComponentScan` (tells Spring to scan the package and its sub-packages for components, configurations, and services).
* **Q**: What is the difference between `application.properties` and `application.yml`?
  * **A**: They serve the same purpose, but `application.yml` uses a hierarchical, indented structure. This makes it easier to read and manage complex configurations compared to the flat key-value pairs in properties files.

---

## 11. Learning-First Analysis

### Why It Exists
Setting up the backend environment correctly ensures that compile-time dependencies, database connection pools, and core security frameworks are properly configured before writing any business logic.

### Java Concepts Used
* **Maven Dependency Management**: Automatically resolving and downloading dependent JAR libraries.
* **JVM Settings**: Target compiling under Java 21 features (like Records and pattern matching).

### Spring Boot Concepts Used
* **Auto-Configuration**: Spring Boot automatically configures features (like HikariCP connection pool or transaction managers) based on the dependencies present in the classpath.
* **Component Scanning**: Automating bean lookup across package directories.

### Connections to Other Modules
* Serves as the configuration host. All JPA entities, controllers, and services require this setup to run.

### Recruiter Evaluation Perspective
Recruiters evaluate your ability to set up clean configurations. They check if application secrets are externalized, if database credentials are clean, and if the project directory is structured according to standard Spring Boot conventions.
