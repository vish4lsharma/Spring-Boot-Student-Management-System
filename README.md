# Spring Boot Student Management System

A production-ready REST API for managing students, built with Java, Spring Boot, Spring Data JPA, and MySQL.

## Features

- Clean layered architecture (Controller, Service, Repository, Entity, DTO, Config)
- Complete CRUD operations for Students
- Jakarta validation limits data corruption
- Centralized exception handling with `@ControllerAdvice`
- Swagger / OpenAPI documentation
- SLF4J logging integration

## Prerequisites

- Java 17
- Maven 3.6+
- MySQL 8.0+

## Database Configuration

Update the `src/main/resources/application.properties` file with your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/studentdb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> Ensure you have an empty schema created in MySQL named `studentdb` before running the application.

```sql
CREATE DATABASE studentdb;
```

## How to Run the Project

1. **Clone the repository:**
   ```bash
   git clone https://github.com/vish4lsharma/Spring-Boot-Student-Management-System.git
   cd Spring-Boot-Student-Management-System
   ```
2. **Build the project:**
   ```bash
   mvn clean install
   ```
3. **Run the project:**
   ```bash
   mvn spring-boot:run
   ```

The application will start on port `8080`.

## API Endpoints

### Student Endpoints

| Method | URL                  | Description          |
|--------|----------------------|----------------------|
| POST   | `/api/students`      | Create a new student |
| GET    | `/api/students`      | Get all students     |
| GET    | `/api/students/{id}` | Get a student by ID  |
| PUT    | `/api/students/{id}` | Update a student     |
| DELETE | `/api/students/{id}` | Delete a student     |

**Standard API Response Structure:**
```json
{
  "success": true,
  "message": "Action completed successfully",
  "data": { ... }
}
```

## Swagger UI Documentation

Access the Swagger UI at:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

Access the OpenAPI documentation in JSON format at:
[http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
