# Section API Design

**Date:** 2025-11-03
**Status:** Approved
**Type:** New Feature Implementation

---

## Overview

Implementation of a Section API following the existing Product API pattern. The Section API will provide standard CRUD operations for managing store sections (departments) within the retail management system.

## Requirements

### Functional Requirements
- Standard CRUD operations (Create, Read, Update, Delete)
- RESTful API following existing Product API conventions
- Flat DTO structure with basic section attributes
- Authentication required for all endpoints

### Non-Functional Requirements
- **Simplicity:** No caching, async operations, or profiling aspects
- **Consistency:** Follow existing architectural patterns (full layered architecture)
- **Transaction Safety:** Proper transaction boundaries on all operations

## Architecture

### Layered Architecture

The Section API follows the standard three-layer architecture established in the Product API:

```
┌─────────────────────────────────────┐
│   SectionController                 │  REST Layer
│   /v1/api/section                   │  - HTTP handling
└─────────────────────────────────────┘  - Request/Response mapping
                ↓
┌─────────────────────────────────────┐
│   SectionService                    │  Business Layer
│   @Transactional                    │  - Transaction management
└─────────────────────────────────────┘  - Entity ↔ DTO conversion
                ↓
┌─────────────────────────────────────┐
│   SectionRepository                 │  Data Layer
│   extends JpaRepository             │  - Database operations
└─────────────────────────────────────┘
```

### Components

| Component | Location | Responsibility |
|-----------|----------|----------------|
| **SectionController** | `controller/SectionController.java` | REST endpoints, HTTP handling |
| **SectionService** | `service/SectionService.java` | Business logic, transactions, conversions |
| **SectionRepository** | `repository/SectionRepository.java` | Data access via Spring Data JPA |
| **SectionDTO** | `dto/SectionDTO.java` | API contract (data transfer object) |
| **Section** | `domain/model/Section.java` | JPA entity (already exists) |

## API Design

### REST Endpoints

**Base Path:** `/v1/api/section`

| Method | Endpoint | Request Body | Response | Description |
|--------|----------|--------------|----------|-------------|
| POST | `/v1/api/section` | SectionDTO | HTTP 200 | Create new section |
| GET | `/v1/api/section/{id}` | - | SectionDTO | Get section by ID |
| GET | `/v1/api/section` | - | List\<SectionDTO\> | Get all sections |
| PUT | `/v1/api/section/{id}` | SectionDTO | HTTP 200 | Update section |
| DELETE | `/v1/api/section/{id}` | - | HTTP 200 | Delete section |

### Data Transfer Object

**SectionDTO** (Java record):
```java
public record SectionDTO(
    int id,        // Section identifier
    String name,   // Section name (e.g., "Electronics", "Clothing")
    int storeId    // Foreign key reference to Store
) {}
```

**Design Rationale:**
- Flat structure keeps it simple and consistent with ProductDTO
- Clients make separate calls for Store or Product details if needed
- Immutable record type for thread safety

### Security

- **Authentication:** HTTP Basic (consistent with Product API)
- **Authorization:** All endpoints require authenticated user
- **Users:** Existing in-memory users (`user/password`, `admin/password`)

### Error Handling

Leverages existing `@ControllerAdvice` in `ExceptionHandlers.java`:

| Error Scenario | HTTP Status | Response |
|----------------|-------------|----------|
| Section not found | 404 Not Found | Error message |
| Invalid Store reference | 400 Bad Request | Validation error |
| Missing required fields | 400 Bad Request | Validation error |
| Authentication failure | 401 Unauthorized | Auth error |

## Service Layer Design

### SectionService Methods

```java
@Service
public class SectionService {

    @Transactional(propagation = Propagation.REQUIRED)
    public SectionDTO create(SectionDTO dto);

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SectionDTO get(int id);

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<SectionDTO> getAll();

    @Transactional(propagation = Propagation.REQUIRED)
    public SectionDTO update(int id, SectionDTO dto);

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(int id);
}
```

### Transaction Strategy

- **Write Operations** (CREATE, UPDATE, DELETE): `Propagation.REQUIRED`
  - Ensures operations run within a transaction
  - Rollback on exceptions

- **Read Operations** (GET, GET ALL): `Propagation.SUPPORTS, readOnly = true`
  - Participates in existing transaction if present
  - Read-only optimization

### Key Implementation Details

1. **Entity ↔ DTO Conversion**
   - Service layer handles all mapping
   - Converts SectionDTO → Section entity before repository operations
   - Converts Section entity → SectionDTO for responses

2. **Validation**
   - Verify Store exists when creating/updating (via storeId)
   - Throw appropriate exceptions for not found scenarios
   - Spring validation on DTO fields

3. **Cascade Behavior**
   - Section deletion triggers cascade delete of all Products
   - Based on existing `@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)`
   - Automatic cleanup via JPA relationship

4. **Simplicity**
   - No caching layer
   - No async operations
   - No profiling aspects
   - Synchronous, straightforward operations

## Data Flow

### Create Section Flow

```
1. Client → POST /v1/api/section
   Body: {"id": 0, "name": "Electronics", "storeId": 1}

2. SectionController receives request

3. SectionController → SectionService.create(dto)

4. SectionService:
   - Validates Store exists
   - Converts DTO → Section entity
   - Calls SectionRepository.save()

5. Repository persists to database

6. SectionService converts saved entity → DTO

7. Controller returns DTO (HTTP 200)
   Response: {"id": 5, "name": "Electronics", "storeId": 1}
```

### Update Section Flow

```
1. Client → PUT /v1/api/section/5
   Body: {"id": 5, "name": "Electronics & Appliances", "storeId": 1}

2. SectionService:
   - Retrieves existing Section by ID
   - Validates Store reference
   - Updates entity fields (name, storeId)
   - Saves changes

3. Returns updated DTO
```

### Delete Section Flow

```
1. Client → DELETE /v1/api/section/5

2. SectionService:
   - Retrieves Section by ID
   - Calls repository.delete()

3. JPA cascade delete:
   - Removes all Products in section
   - Removes Section record

4. Returns success (HTTP 200)
```

## Testing Strategy

Following the existing TestNG + REST Assured pattern from Product API:

### Unit Tests: SectionServiceTest

**Test Class:** `src/test/java/.../service/SectionServiceTest.java`

**Test Cases:**
- `testCreateSection()` - Verify section creation
- `testGetSection()` - Verify retrieval by ID
- `testGetAllSections()` - Verify list all sections
- `testUpdateSection()` - Verify section updates
- `testDeleteSection()` - Verify deletion
- `testDeleteSectionCascadesToProducts()` - Verify cascade behavior
- `testCreateSectionWithInvalidStore()` - Verify validation

**Framework:**
- TestNG with `@Test` annotations
- `@DataProvider` for parameterized tests
- Mock dependencies if needed

### Integration Tests: SectionRESTControllerTest

**Test Class:** `src/test/java/.../controller/SectionRESTControllerTest.java`

**Test Cases:**
- `testCreateSectionViaAPI()` - POST endpoint
- `testGetSectionViaAPI()` - GET by ID endpoint
- `testGetAllSectionsViaAPI()` - GET all endpoint
- `testUpdateSectionViaAPI()` - PUT endpoint
- `testDeleteSectionViaAPI()` - DELETE endpoint
- `testAuthenticationRequired()` - Security verification

**Framework:**
- `@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)`
- `@ActiveProfiles(Profiles.IN_MEMORY)`
- REST Assured for fluent API testing
- Base class: `AbstractTransactionalTestNGSpringContextTests`

**Example Test Structure:**
```java
@Test
public void testCreateSectionViaAPI() {
    SectionDTO dto = new SectionDTO(0, "Electronics", 1);

    given()
        .auth().basic("user", "password")
        .contentType("application/json")
        .body(dto)
    .when()
        .post("/v1/api/section")
    .then()
        .statusCode(200)
        .body("name", equalTo("Electronics"))
        .body("storeId", equalTo(1));
}
```

## Implementation Checklist

- [ ] Create SectionDTO record in `dto/SectionDTO.java`
- [ ] Create SectionRepository interface in `repository/SectionRepository.java`
- [ ] Implement SectionService in `service/SectionService.java`
- [ ] Implement SectionController in `controller/SectionController.java`
- [ ] Write SectionServiceTest
- [ ] Write SectionRESTControllerTest
- [ ] Verify all tests pass
- [ ] Verify build succeeds

## Dependencies

### Existing Code (No Changes Required)
- Section entity (`domain/model/Section.java`)
- Store entity (`domain/model/Store.java`)
- Product entity (`domain/model/Product.java`)
- ExceptionHandlers (`errorhandling/ExceptionHandlers.java`)
- SecurityConfiguration (`config/SecurityConfiguration.java`)

### New Code to Create
- SectionDTO
- SectionRepository
- SectionService
- SectionController
- SectionServiceTest
- SectionRESTControllerTest

## Success Criteria

1. ✅ All endpoints functional and follow REST conventions
2. ✅ All tests pass (unit + integration)
3. ✅ Build succeeds without errors
4. ✅ Consistent with Product API patterns
5. ✅ Proper transaction management
6. ✅ Security enforced on all endpoints
7. ✅ Error handling working correctly
8. ✅ Cascade delete behavior verified

---

## Appendix: Code Examples

### SectionDTO
```java
package com.nix.reference.spring.project.dto;

public record SectionDTO(int id, String name, int storeId) {}
```

### SectionRepository
```java
package com.nix.reference.spring.project.repository;

import com.nix.reference.spring.project.domain.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {
}
```

### Controller Method Example
```java
@PostMapping
public SectionDTO create(@RequestBody SectionDTO dto) {
    return sectionService.create(dto);
}
```
