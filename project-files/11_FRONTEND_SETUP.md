# 11 Frontend Setup - Academic Management System (AMS)

## 1. Purpose of the File
This file guides the implementation of the client-side user interface, detailing the structure of static HTML/CSS pages, Bootstrap integration, and JavaScript fetch routines for REST APIs.

## 2. Learning Objectives
* Connect a frontend UI to secure Spring Boot REST APIs.
* Store and manage JWT access tokens securely using browser storage.
* Render API data dynamically in the DOM using Vanilla JavaScript.

## 3. Prerequisite Concepts
* HTML5 DOM manipulation.
* Styling layouts with Bootstrap 5 utility classes.
* Handling asynchronous requests in JavaScript (Promises, async/await).

## 4. Implementation Steps
1. Create a `static` resources directory in the Spring Boot project.
2. Build layout templates for the login screen and user-specific dashboards (Admin, Faculty, Student).
3. Write a JavaScript utility to manage JWT storage (e.g., in `localStorage`) and attach token headers to requests.
4. Write asynchronous JavaScript functions to handle login submissions and redirect users based on roles.
5. Implement dynamic rendering functions to populate dashboard tables and charts.

## 5. Files and Folders Created
* `src/main/resources/static/index.html` (Application entrance router)
* `src/main/resources/static/login.html` (Login page layout)
* `src/main/resources/static/admin.html` (Admin dashboard panel)
* `src/main/resources/static/faculty.html` (Faculty dashboard panel)
* `src/main/resources/static/student.html` (Student dashboard panel)
* `src/main/resources/static/js/app.js` (Core JavaScript routines)

## 6. Spring Boot Annotations Required
None directly (static resources are served automatically by Spring Boot from the static classpath folder).

## 7. Database Tables Involved
This setup communicates with all database tables through backend REST APIs.

## 8. API Endpoints Involved
All backend REST APIs.

## 9. Common Mistakes
* **CORS Blockages**: Forgetting to configure Cross-Origin Resource Sharing (CORS) in Spring Security, which blocks frontend requests.
* **Exposing Tokens in URLs**: Passing JWT tokens as URL parameters instead of attaching them to the HTTP `Authorization` header.
* **Lack of Authorization Routing**: Allowing unauthenticated users to access dashboard views by skipping local JavaScript authentication checks.

## 10. Interview Questions Related to this Module
* **Q**: What is CORS, and why does the browser block requests from different origins?
  * **A**: CORS stands for Cross-Origin Resource Sharing. It is a browser security mechanism that restricts web pages from making requests to a different domain than the one that served the page. We can resolve this by configuring a CORS policy in Spring Security to explicitly authorize specific origins.
* **Q**: Where should you store JWT tokens in the browser, and what are the security trade-offs of each option?
  * **A**: Storing tokens in `localStorage` protects them from page refreshes but makes them vulnerable to Cross-Site Scripting (XSS) attacks. Storing them in memory (e.g., in a JavaScript variable) is more secure but loses the token when the user refreshes the page. For enterprise applications, using HttpOnly cookies is the most secure option because it prevents JavaScript access entirely, protecting against XSS.

---

## 11. Learning-First Analysis

### Why It Exists
A user interface connects human users to backend business logic. An intuitive, responsive dashboard ensures that administrators, faculty, and students can access records easily.

### Java Concepts Used
* **Spring MVC Static Resource Resolution**: Spring Boot automatically configures static resources from the `classpath:/static/` directory to serve files like HTML, CSS, and JS.

### Spring Boot Concepts Used
* **CORS Configurations**: Setting up CORS mappings in the security configuration to authorize frontend requests.

### Connections to Other Modules
* Integrates with all backend API modules (Authentication, Student, Faculty, Course, Attendance, Results) by sending HTTP requests and rendering responses.

### Recruiter Evaluation Perspective
Recruiters evaluate how the frontend interacts with backend APIs. They check if tokens are handled securely, if API errors are handled gracefully on the client side, and if dynamic content is rendered cleanly using standard JavaScript.
