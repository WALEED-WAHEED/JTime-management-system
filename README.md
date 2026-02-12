# JTime Management System

JTime is a time and task management application designed to facilitate planning and monitoring of activities.

## Features

- **Project Management:** Create and view active projects. Complete projects only when all tasks are finished.
- **Task Management:** Associate tasks with projects, estimate time (hours), and record actual duration.
- **Daily Planning:** Interface to assign tasks to specific days with automatic effort calculation.
- **Reporting:** Summaries organized by project and time interval, comparing estimated vs actual time.

## Tech Stack

- **Java 21** (utilizing sealed interfaces, switch expressions, and streams)
- **JavaFX** for the User Interface
- **Gradle** for build and dependency management
- **Hibernate / JPA** for persistence
- **H2 Database** (in-memory)
- **Lombok** to reduce boilerplate
- **JUnit 5** for testing

## Architecture

The project follows SOLID principles and a layered architecture:
- **Model Layer:** Business logic and data integrity.
- **Persistence Layer:** JPA/Hibernate for data storage (separated from domain model via Mappers).
- **Controller/UI Layer:** Manages user interaction.
- **Manager (Composition):** Coordinates complex operations across multiple entities.

## Getting Started

### Prerequisites

- Java 21 JDK or higher
- Gradle (optional, a wrapper is included)

### Running the Application

To run the application, use the following command:

```bash
./gradlew run
```

On Windows:

```powershell
.\gradlew.bat run
```

### Running Tests

To execute unit tests:

```bash
./gradlew test
```
