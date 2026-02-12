# JTime Management System

JTime is a modern Java-based application designed for efficient time and activity management. It facilitates planning, monitoring, and reporting of commitments through a structured approach and clean architecture.

## 🚀 Key Features

- **Project Management:** Create and track active projects. Built-in validation ensures projects can only be closed when all tasks are complete.
- **Task Tracking:** Associate tasks with projects, set time estimates (hours), and record actual duration upon completion.
- **Daily Planning:** Assign tasks to specific days with automatic calculation of total daily effort.
- **Advanced Reporting:** Generate project-based and time-based reports comparing estimated vs. actual time.

## 🏗️ Architecture & Design

The system is built following best practices in software engineering:

- **SOLID Principles:** High extensibility through interfaces and composition.
- **Layered Architecture:**
  - **Model:** Business logic and data integrity.
  - **Persistence:** Clean separation using JPA/Hibernate.
  - **UI/Controller:** Interactive user interface management.
- **Interface-First Design:** Core components like `Taggable`, `TimeTrackable`, and `JTimeManager` define clear contracts for system behavior.

## 🛠️ Technology Stack

- **Java 21:** Utilizing modern features like Sealed Interfaces, Records, and Stream API.
- **JPA / Hibernate:** ORM for data persistence.
- **H2 Database:** In-memory database for rapid prototyping.
- **Gradle:** Build automation and dependency management.

## 📂 Project Structure

- `src/main/java`: Core application logic and implementations.
- `src/main/resources`: Configuration files and UI definitions (FXML).
- `relazione.md`: Detailed technical documentation and project requirements.
- `.gitignore`: Configured to exclude build artifacts, IDE settings, and large sample files.

---
*Created as part of the initial repository setup.*
