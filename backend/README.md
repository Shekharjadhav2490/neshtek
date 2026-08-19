# Neshtek Monitor API

Spring Boot 3 backend for Neshtek Monitor.

## Phase 4B.2 foundation

- Java 17
- Spring Boot 3.5.x
- Spring Web
- Spring Data JPA
- Bean Validation
- Actuator
- Oracle JDBC

## Run locally

Set the database environment variables before starting the application:

```bash
export DB_URL='jdbc:oracle:thin:@//localhost:1521/FREEPDB1'
export DB_USERNAME='neshtek'
export DB_PASSWORD='change-me'
```

Then run:

```bash
mvn spring-boot:run
```

Health endpoint:

`GET http://localhost:8080/api/v1/health`

Actuator health:

`GET http://localhost:8080/actuator/health`

Database schema and monitoring entities will be added in the next Phase 4B.2 step.
