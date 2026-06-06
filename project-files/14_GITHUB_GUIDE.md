# 14 GitHub Guide - Academic Management System (AMS)

## 1. Purpose of the File
This file guides the user through version control best practices, GitFlow branching models, conventional commit guidelines, and README.md formatting structures.

## 2. Learning Objectives
* Use Git version control configurations.
* Implement the GitFlow branching model (main, develop, feature/*).
* Write structured commit messages using the Conventional Commits specification.

## 3. Prerequisite Concepts
* Git command line operations.
* Working with remote repositories (GitHub, GitLab).
* Resolving merge conflicts.

## 4. Implementation Steps
1. Initialize the project repository: `git init`.
2. Add a `.gitignore` file to exclude build directories (`/target/`, `.idea/`, `.env`, and IDE setting files).
3. Create the `develop` branch from `main`: `git checkout -b develop`.
4. Create feature-specific branches (e.g., `feature/auth-setup`) for each development task.
5. Merge feature branches into `develop` using pull requests, and merge to `main` for release tags.

## 5. Files and Folders Created
* `.gitignore` (Excludes build artifacts and secrets)
* `README.md` (Main repository homepage documentation)

## 6. Spring Boot Annotations Required
None (Git configuration).

## 7. Database Tables Involved
None.

## 8. API Endpoints Involved
None.

## 9. Common Mistakes
* **Committing Secret Keys**: Committing database passwords or private JWT keys to public repositories. Always use external environment variables.
* **Committing Build Artifacts**: Including the `/target/` folder in commits, which makes the repository size larger and can cause merge conflicts.
* **Unstructured Commit History**: Writing vague commit messages (e.g., "fixed bug" or "done"). Use clear, structured messages (e.g., `fix(auth): update token expiration logic`).

## 10. Interview Questions Related to this Module
* **Q**: What is the difference between Git and GitHub?
  * **A**: Git is a local version control system that tracks code changes and history. GitHub is a cloud-based hosting platform for Git repositories that provides collaboration tools, pull request reviews, and issue tracking.
* **Q**: What branching strategy do you use when collaborating on a team, and why?
  * **A**: I use the GitFlow branching strategy. It isolates active feature development (in `feature/*` branches) from the integration branch (`develop`), and keeps the production branch (`main`) clean and stable. This prevents unfinished work from breaking production releases.

---

## 11. Learning-First Analysis

### Why It Exists
Version control tracks code history and enables team collaboration. Following structured branching and commit standards keeps the repository clean and manageable.

### Java Concepts Used
* **Build Exclusions**: Excluding compiled `.class` files and dependencies from version control using `.gitignore`.

### Spring Boot Concepts Used
* **Profile Configuration**: Keeping environment-specific configs separate using profile YAML files, and keeping passwords out of version control.

### Connections to Other Modules
* Manages the code repository and development workflows for all modules.

### Recruiter Evaluation Perspective
Recruiters evaluate your Git history to check for professional coding habits. They look for atomic, structured commits (using Conventional Commits), logical branch structures, and a well-written README that explains how to run the project.
