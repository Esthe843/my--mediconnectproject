MediConnect — Smart Healthcare Appointment & Medical Record Management System
=============================================================================

Tech Stack
----------
- Backend  : Java 21+, Spring Boot 3.2, Spring MVC, Spring Data JPA, Hibernate
- Security : Spring Security + JWT (jjwt 0.12)
- Database : MySQL 8
- Docs     : SpringDoc OpenAPI (Swagger UI)
- Build    : Maven
- Lombok   : Boilerplate reduction

Prerequisites
-------------
- JDK 21+
- Maven 3.9+
- MySQL 8+
- An IDE (IntelliJ IDEA / VS Code)

Database Setup
--------------
Run the following SQL commands in your MySQL client:

    CREATE DATABASE IF NOT EXISTS mediconnect
        CHARACTER SET utf8mb4
        COLLATE utf8mb4_unicode_ci;

Then update the environment variables (or edit application.yml):

    export DB_USERNAME=root
    export DB_PASSWORD=yourpassword

JWT Secrets (env vars — never hard-code):

    export JWT_SECRET=<base64-encoded-secret-at-least-256-bits>
    export JWT_EXPIRATION=86400000

Run the Application
-------------------
    cd MediConnect
    mvn spring-boot:run

The app starts on http://localhost:8080

Swagger UI:
    http://localhost:8080/swagger-ui.html

Project Structure
-----------------
    MediConnect/
    ├── src/main/java/com/mediconnect/
    │   ├── MediConnectApplication.java
    │   ├── config/         (SecurityConfig, AuditingConfig, etc.)
    │   ├── controller/     (REST controllers)
    │   ├── dto/            (request/response DTOs)
    │   ├── entity/         (JPA entities)
    │   ├── exception/      (custom exceptions)
    │   ├── repository/     (Spring Data JPA repositories)
    │   ├── security/       (JWT filter, token provider)
    │   ├── service/        (business logic)
    │   │   └── impl/       (service implementations)
    │   └── util/           (utility classes)
    ├── src/main/resources/
    │   └── application.yml
    ├── frontend/
    │   ├── html/
    │   ├── css/
    │   ├── js/
    │   └── assets/
    ├── pom.xml
    └── README.md

Database Schema
---------------
7 tables auto-created by Hibernate on startup:

    users            — Authentication & role management
    patients         — Patient profile (1:1 with users)
    doctors          — Doctor profile (1:1 with users)
    appointments     — Booking records (M:1 with patients & doctors)
    medical_records  — Diagnosis & treatment logs (M:1 with patients, doctors, appointments)
    prescriptions    — Medication prescriptions (M:1 with medical_records)
    notifications    — In-app notifications (M:1 with users)

Next Steps
----------
- [ ] JWT authentication filter & token provider
- [ ] Auth controller (register / login)
- [ ] Role-based access control
- [ ] Service layer + business logic
- [ ] REST controllers with validation
- [ ] Global exception handler
- [ ] Frontend (HTML/CSS/JS/Bootstrap)
