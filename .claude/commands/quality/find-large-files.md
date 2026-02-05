---
command: quality:find-large-files
description: Find top 5 largest files and recommend how to split them
---

## Description

Searches the codebase for the top 5 largest files (by line count), displays them with details, and provides actionable recommendations on how to split them for better maintainability.

## Execution Steps

1. **Find Large Files**
   - Search for `.java` files in `src/`
   - Exclude `target/`, `build/`, and generated files
   - Sort by line count descending
   - Select top 5 largest files

2. **Analyze Each File**
   - Count total lines
   - Identify file type (controller, service, repository, entity, config, etc.)
   - Identify major sections (methods, inner classes, etc.)
   - Note any code smells (too many responsibilities, mixed concerns)

3. **Generate Recommendations**
   For each large file, provide:
   - Current line count and file type
   - Identified concerns/responsibilities
   - Specific splitting recommendations
   - Suggested new file names

4. **Display Results**
   Present findings in a clear, actionable format

## Output Format

```markdown
# Top 5 Largest Files Analysis

## 1. {filename} ({line_count} lines)

**Type:** {controller|service|repository|entity|config|etc.}

**Current Structure:**
- {description of major sections}

**Recommendations:**
- [ ] {specific action 1}
- [ ] {specific action 2}

**Suggested Split:**
- `{NewClass1.java}` - {responsibility}
- `{NewClass2.java}` - {responsibility}

---

## 2. {filename} ({line_count} lines)
...
```

## Splitting Guidelines

### Controllers
- Extract complex request/response mapping to dedicated DTOs
- Split by resource groupings (e.g., UserController, UserPreferencesController)
- Move validation logic to separate validator classes
- Extract business logic to services

### Services
- Split by domain responsibility (Single Responsibility Principle)
- Extract shared utilities to helper classes
- Separate data transformation logic to mappers
- Create dedicated exception handling classes

### Repositories
- Use Spring Data query methods instead of custom implementations
- Extract complex queries to custom repository implementations
- Split by aggregate root

### Entities
- Extract embedded value objects
- Move complex business logic to domain services
- Separate audit fields to base entity class

### Configuration Classes
- Split by concern (security, async, cache, datasource)
- Extract bean definitions to focused configs
- Move properties to dedicated @ConfigurationProperties classes

## Thresholds

| File Type | Ideal Max Lines | Warning | Critical |
|-----------|-----------------|---------|----------|
| Controller | 150 | 250 | 400+ |
| Service | 200 | 350 | 500+ |
| Repository | 100 | 200 | 300+ |
| Entity | 150 | 250 | 400+ |
| Config | 100 | 150 | 250+ |

## Notes

- Focus on actionable recommendations
- Consider existing project patterns when suggesting splits
- Prioritize recommendations by impact and effort
- Line count includes comments and whitespace
