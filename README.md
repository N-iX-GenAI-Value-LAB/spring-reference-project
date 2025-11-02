# Spring Boot Reference Project

A complete Spring Boot 3.5.6 reference application demonstrating enterprise-grade patterns and best practices for building REST APIs with Spring Framework.

[![Tests](https://github.com/N-iX-GenAI-Value-LAB/spring-reference-project/actions/workflows/test-on-pr.yml/badge.svg?event=project)](https://github.com/N-iX-GenAI-Value-LAB/spring-reference-project/actions/workflows/test-on-pr.yml)

## Quick Start

### Prerequisites
- Java 21
- Maven 3.x

### Run the Application

```bash
# Build the project
mvn clean package

# Run the application
mvn spring-boot:run
```

The application will start on a random port with the H2 in-memory database.

### Run Tests

```bash
# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=ProductControllerTest
```

## Technology Stack

- **Java 21**
- **Spring Boot 3.5.6**
- **Spring Data JPA** - Data persistence
- **Spring Security** - Authentication & authorization
- **H2 Database** - In-memory database
- **AspectJ** - Cross-cutting concerns (logging, profiling)
- **TestNG** - Testing framework
- **REST Assured** - API testing

## Project Structure

```
src/main/java/com/nix/reference/spring/project/
├── controller/       # REST API endpoints
├── service/          # Business logic
├── repository/       # Data access layer
├── domain/model/     # JPA entities
├── dto/              # Data transfer objects
├── config/           # Spring configuration
├── security/         # Security configuration
└── aop/              # Aspect-oriented programming
```

## API Endpoints

Base URL: `/v1/api`

### Product API
- `POST /v1/api/product` - Create a product
- `GET /v1/api/product/{id}` - Get product by ID
- `GET /v1/api/product` - Get all products
- `PUT /v1/api/product/{id}` - Update product
- `DELETE /v1/api/product/{id}` - Delete product

### Authentication

The application uses HTTP Basic authentication:
- **User**: `user` / `password` (role: USER)
- **Admin**: `admin` / `password` (roles: USER, ADMIN)

## Development

### Profiles

The application supports multiple profiles:
- `dev` - Development (default)
- `prod` - Production
- `in-memory` - Testing with in-memory database

### Database

H2 console available at runtime for database inspection.

## Further Information

For detailed architecture information, development guidelines, and advanced patterns used in this project, see [CLAUDE.md](./CLAUDE.md).
