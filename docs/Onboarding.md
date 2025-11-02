# Spring Boot Reference Project - Onboarding Guide

Welcome to the Spring Boot Reference Project! This guide will help you get up to speed quickly.

## What This Project Is

This is an **enterprise-grade Spring Boot 3.5.6 reference application** that demonstrates best practices for building REST APIs. It's a complete working example of a Product Management system with authentication, persistence, and advanced Spring features.

Think of it as a **blueprint** for building production-ready Spring applications - it showcases patterns you'll use in real-world projects.

---

## Technology Stack

**Core Technologies:**
- Java 21
- Spring Boot 3.5.6
- Spring Data JPA (for database access)
- Spring Security (authentication & authorization)
- H2 Database (in-memory for development)
- AspectJ (cross-cutting concerns like logging & profiling)

**Testing:**
- TestNG (not JUnit!)
- REST Assured (API testing)

---

## Quick Start

### Prerequisites
```bash
# Check Java version (must be 21)
java -version

# Check Maven version (3.x)
mvn -version
```

### Running the Application
```bash
# Build and run in one command
mvn spring-boot:run

# Or build first, then run the JAR
mvn clean package
java -jar target/spring-boot-training-1.0-SNAPSHOT.jar
```

The app starts on a **random port** with an H2 in-memory database.

### Running Tests
```bash
# All tests
mvn test

# Specific test class
mvn test -Dtest=ProductControllerTest

# Specific test method
mvn test -Dtest=ProductControllerTest#givenTheContentTypeIsCorrect_WhenGettingAProduct_ThenAllGood
```

---

## Project Architecture

### Layered Architecture Pattern

The project follows clean separation of concerns:

```
controller/       → REST API endpoints (entry point)
    ↓
service/          → Business logic & transaction management
    ↓
repository/       → Data access layer (Spring Data JPA)
    ↓
domain/model/     → JPA entities (database tables)
```

**Supporting layers:**
- `dto/` - Data Transfer Objects (API contracts)
- `config/` - Spring configuration classes
- `security/` - Authentication & authorization
- `aop/` - Cross-cutting concerns (logging, profiling)
- `errorhandling/` - Centralized exception handling

### Code Organization

**Base Package:** `com.nix.reference.spring.project`

**Entry Point:** `SpringReferenceProject.java:14` - Main application class

---

## Key Components Explained

### 1. **Controllers** (REST API Layer)

Example: `ProductController.java:25`

```java
@RestController
@RequestMapping(path = "/v1/api/product")
public class ProductController {
    // Handles HTTP requests: POST, GET, PUT, DELETE
}
```

**Responsibilities:**
- Handle HTTP requests/responses
- Validate input (DTOs)
- Delegate to service layer
- Return appropriate HTTP status codes

**API Base Path:** `/v1/api`

### 2. **Services** (Business Logic Layer)

Services contain your core business logic and handle transactions.

**Key Patterns:**
- `@Transactional` for database operations
- `@Async` for asynchronous processing (e.g., `ProductService.update()`)
- `@Cacheable` for caching (e.g., `ProductService.get()` uses "products" cache)

**Transaction Propagation:**
- `Propagation.REQUIRED` - Write operations
- `Propagation.SUPPORTS` - Read operations

### 3. **Repositories** (Data Access Layer)

Spring Data JPA repositories - no SQL needed!

```java
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // Spring generates implementation automatically
}
```

### 4. **Domain Models** (Entities)

JPA entities map to database tables. Example: `Product.java:8`

**Key Relationships:**
- `Product` → `Section` (Many-to-One) at `Product.java:22`
- `Store` ↔ `Manager` via `StoreManager` (Composite key pattern)

**Base Class:** All entities extend `AbstractEntity` for common fields.

### 5. **DTOs** (Data Transfer Objects)

Used for API requests/responses - **never expose entities directly** to the API!

---

## Advanced Features You'll Encounter

### Spring Security

**Configuration:** `config/SecurityConfiguration.java`

**Authentication:**
- HTTP Basic Auth
- In-memory users (for demo purposes):
  - `user` / `password` (role: USER)
  - `admin` / `password` (roles: USER, ADMIN)

**Endpoints:**
- Public: `/v1/api/auth/**`, `/v1/api/login`, `/register`
- Protected: Everything else (requires authentication)

**Security Annotations:**
- `@PreAuthorize` - Method-level security
- `@HasManagerRole` - Custom meta-annotation

### Aspect-Oriented Programming (AOP)

**Custom Profiling Annotations:**
- `@ExecutionTimeProfiling` - Measures method execution time (see `ProductController.java:53`)
- `@MemoryProfiling` - Tracks memory usage (see `ProductController.java:47`)

**Aspects:** `aop/aspect/ProfilingAspect.java` and `LoggingAspect.java`

**Configuration:** `config/AspectJConfig.java`

### Async Processing

**Configuration:** `config/AsyncConfig.java`

Methods annotated with `@Async` run in separate threads (e.g., product updates).

### Caching

Spring Cache abstraction with key-based caching:
```java
@Cacheable(value = "products", key = "#id")
public ProductDTO get(int id) { ... }
```

---

## Application Profiles

Defined in `Profiles.java`:
- **dev** - Development (default)
- **in-memory** - Testing
- **prod** - Production

**Configuration files:**
- `application.yml` - Common config
- `application-dev.yml` - Dev-specific
- `application-prod.yml` - Prod-specific

---

## REST API Quick Reference

**Base URL:** `/v1/api`

### Product Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/v1/api/product` | Create product |
| GET | `/v1/api/product/{id}` | Get product by ID |
| GET | `/v1/api/product` | Get all products |
| PUT | `/v1/api/product/{id}` | Update product |
| DELETE | `/v1/api/product/{id}` | Delete product |

### Testing APIs

Use credentials: `user:password` or `admin:password` with HTTP Basic Auth.

---

## Testing Strategy

**Framework:** TestNG (not JUnit!)

**Base Classes:**
- `AbstractTransactionalTestNGSpringContextTests` - For integration tests
- `@SpringBootTest(webEnvironment = RANDOM_PORT)` - Starts full Spring context

**Key Annotations:**
- `@Test` - Mark test methods
- `@DataProvider` - Parameterized tests
- `@ActiveProfiles(Profiles.IN_MEMORY)` - Use in-memory profile

**REST Assured:** Used for API testing with fluent assertions

```java
given()
    .contentType(ContentType.JSON)
    .when()
    .get("/v1/api/product/1")
    .then()
    .statusCode(200);
```

---

## Database Configuration

**H2 In-Memory Database:**
- JDBC URL: `jdbc:h2:mem:complete-web-project`
- Console: Available at runtime for inspection
- Schema: Auto-created on startup (`create-drop` mode)

**Important:** `spring.jpa.open-in-view = false` - Explicit transaction boundaries required!

---

## Development Workflow

1. **Start the app:** `mvn spring-boot:run`
2. **Make changes** in your IDE
3. **Run tests:** `mvn test`
4. **Build:** `mvn clean package`
5. **Commit** with descriptive messages

---

## Common Patterns & Conventions

1. **Exception Handling:** Centralized in `errorhandling/ExceptionHandlers.java` using `@ControllerAdvice`
2. **Dependency Injection:** Constructor injection (preferred over field injection)
3. **Functional Programming:** Services use `Function`, `Predicate`, `BiFunction` for transformations
4. **Logging:** SLF4J with Logback (configured in `application.yml`)
5. **Immutability:** Prefer immutable objects where possible

---

## Project Statistics

- **Total Java files:** 36
- **Controllers:** 2
- **Services:** 2
- **Domain Models:** 8
- **Test Framework:** TestNG with REST Assured

---

## Next Steps

1. **Explore the code:**
   - Start with `SpringReferenceProject.java:14`
   - Read through `ProductController.java:25`
   - Examine `Product.java:8` to understand entities

2. **Run tests:**
   ```bash
   mvn test
   ```

3. **Try the API:**
   - Start the app
   - Use Postman/curl to test endpoints
   - Authenticate with `user:password`

4. **Deep dive:**
   - Read `CLAUDE.md` for detailed architecture
   - Review `SecurityConfiguration.java` for auth patterns
   - Examine aspects in `aop/aspect/`

---

## Questions?

- **Architecture details:** See `CLAUDE.md`
- **API docs:** Check README.md
- **Build issues:** Run `mvn clean install -U`

Welcome aboard! 🚀
