# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.5.6 reference project demonstrating end-to-end implementation patterns for enterprise applications. It uses Java 21, Spring Data JPA with H2 database, Spring Security, and AspectJ for cross-cutting concerns.

**Main Application Entry Point**: `com.nix.reference.spring.project.SpringReferenceProject`

## Build & Development Commands

### Build and Package
```bash
mvn clean package
```

### Run the Application
```bash
mvn spring-boot:run
```

Or run the packaged JAR:
```bash
java -jar target/spring-boot-training-1.0-SNAPSHOT.jar
```

### Run Tests
```bash
# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=ProductControllerTest

# Run a specific test method
mvn test -Dtest=ProductControllerTest#givenTheContentTypeIsCorrect_WhenGettingAProduct_ThenAllGood
```

### Clean Build Directory
```bash
mvn clean
```

## Architecture & Code Structure

### Layered Architecture
The project follows a standard layered architecture:
- **Controllers** (`controller/`): REST endpoints exposing the API at `/v1/api/*`
- **Services** (`service/`): Business logic layer with transaction management
- **Repositories** (`repository/`): Spring Data JPA repositories for data access
- **Domain Models** (`domain/model/`): JPA entities
- **DTOs** (`dto/`): Data transfer objects for API contracts

### Key Architectural Patterns

**Transaction Management**: Services use `@Transactional` with explicit propagation strategies:
- `Propagation.REQUIRED` for write operations
- `Propagation.SUPPORTS` for read operations

**Async Processing**: The `ProductService.update()` method uses `@Async` for asynchronous execution. Async configuration is in `config/AsyncConfig.java`.

**Caching**: The `ProductService.get()` method uses Spring Cache with key `#id`. Cache name is "products".

**AOP & Cross-Cutting Concerns**:
- Custom profiling annotations: `@ExecutionTimeProfiling` and `@MemoryProfiling`
- Aspects are defined in `aop/aspect/ProfilingAspect.java` and `aop/aspect/LoggingAspect.java`
- Configuration: `config/AspectJConfig.java`

### Security Configuration

Spring Security is configured in `config/SecurityConfiguration.java`:
- HTTP Basic authentication enabled
- CSRF disabled, CORS disabled
- Stateless session management
- In-memory user store with two users:
  - `user` / `password` (role: USER)
  - `admin` / `password` (roles: USER, ADMIN)
- Public endpoints: `/v1/api/auth/**`, `/v1/api/login`, `/register`
- All other endpoints require authentication

**Custom Security Annotations**:
- `@HasManagerRole`: Custom meta-annotation for role-based access
- Use `@PreAuthorize`, `@Secured`, or custom annotations for method-level security

### Domain Model Relationships

**Product** ↔ **Section**: Many-to-One relationship
- Product has `@ManyToOne` to Section
- Lazy fetching with cascade ALL

**Store** ↔ **Manager**: Composite key relationship via **StoreManager**
- Uses `@IdClass(StoreManagerPK.class)` for composite primary key

### Application Profiles

Defined in `Profiles.java`:
- **dev**: Development profile (default in main method)
- **in-memory**: In-memory database profile used in tests
- **prod**: Production profile

Profile-specific configurations in `application-{profile}.yml`.

### REST API Structure

Base API path: `/v1/api`
- Product endpoints: `/v1/api/product`
- CRUD operations follow standard REST conventions:
  - POST `/v1/api/product` - Create
  - GET `/v1/api/product/{id}` - Read single
  - GET `/v1/api/product` - Read all
  - PUT `/v1/api/product/{id}` - Update
  - DELETE `/v1/api/product/{id}` - Delete

### Testing Strategy

Tests use **TestNG** (not JUnit) with **REST Assured**:
- `@SpringBootTest` with `WebEnvironment.RANDOM_PORT`
- `AbstractTransactionalTestNGSpringContextTests` base class for integration tests
- REST Assured for API testing with fluent assertions
- `@DataProvider` for parameterized tests
- `@ActiveProfiles(Profiles.IN_MEMORY)` for test isolation

### Database Configuration

- **Driver**: H2 in-memory database
- **URL**: `jdbc:h2:mem:complete-web-project;DB_CLOSE_ON_EXIT=FALSE`
- **Hibernate DDL**: `create-drop` (recreates schema on startup)
- **JPA open-in-view**: Disabled (explicit transaction boundaries)

### Logging

- SLF4J with Logback
- Custom console pattern in `application.yml`
- Default log level: INFO
- Hibernate SQL logging: Disabled by default (can be enabled by changing `hibernate.sql` to `debug`)

## Important Conventions

**Exception Handling**: Centralized in `errorhandling/ExceptionHandlers.java` using `@ControllerAdvice`.

**Entity Base Class**: All entities extend `AbstractEntity` which provides common fields/behavior.

**Repository Pattern**: All repositories extend Spring Data's `JpaRepository` or `CrudRepository`.

**Functional Programming**: Services use Java functional interfaces (Function, Predicate, BiFunction) for transformations and filters.
