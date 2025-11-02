---
description: Define feature requirements based on description, API contracts, and DB schema
---

# Define Requirements

You are tasked with defining comprehensive requirements for a feature in the AVE AI Agent project.

## Input

The user will provide a feature description as the command argument.

## Your Process

1. **Analyze the Description**
   - Read and understand the feature description provided by the user
   - Review the API contracts from `rest-api-contracts.yaml`
   - Review the database schema from `src/lib/core/db/schema.ts`
   - Identify which endpoints, services, and database tables are relevant

2. **Ask Clarifying Questions**
   - Use the AskUserQuestion tool to gather missing information
   - Focus on:
     - API behavior (request/response formats, status codes, error handling)
     - Database operations (what tables are affected, what fields are needed)
     - Business logic (validation rules, edge cases, constraints)
     - Performance requirements (caching, indexing, pagination)
     - Security considerations (authentication, authorization, data privacy)
   - Ask 2-4 focused questions at a time until you have a complete understanding

3. **Create Requirements Document**
   - Create a markdown file in `docs/requirements/` with a short, descriptive name (e.g., `caching-requirements.md`, `title-generation-requirements.md`)
   - Use the following structure:

```markdown
# [Feature Name] Requirements

## Overview
Brief description of the feature and its purpose.

## API Contracts
### Affected Endpoints
- `[METHOD] /path` - Description of what changes/is added
  - Request format
  - Response format
  - Status codes
  - Error responses

### Request/Response Examples
Provide concrete examples with sample data.

## Database Schema
### Affected Tables
- **table_name**
  - Fields: field1, field2, field3
  - Changes: what's being added/modified
  - Relationships: foreign keys, cascade behavior
  - Indexes: what indexes are needed for performance

### Data Migration
If schema changes are needed:
- Migration strategy
- Backwards compatibility considerations
- Data transformation requirements

## Business Logic
### Functional Requirements
1. Requirement 1: Clear, testable statement
2. Requirement 2: Clear, testable statement
3. ...

### Validation Rules
- Input validation requirements
- Data constraints
- Business rule validation

### Edge Cases
- Edge case 1 and expected behavior
- Edge case 2 and expected behavior
- ...

## Performance Requirements
- Caching strategy (if applicable)
- Query optimization needs
- Expected response times
- Pagination requirements

## Security & Privacy
- Authentication/authorization requirements
- Data access controls
- PII handling
- Rate limiting

## Error Handling
- Expected error scenarios
- Error messages
- Recovery strategies
- Logging requirements

## Testing Requirements
- Unit test scenarios
- Integration test scenarios
- Edge cases to test
- Performance test criteria

## Dependencies
- External services
- Internal services
- Database dependencies
- Third-party libraries

## Out of Scope
List what is explicitly NOT included in this feature.

## Open Questions
Any remaining uncertainties or decisions needed.
```

## Output

After creating the requirements document, inform the user:
- The path to the created file
- A brief summary of the defined requirements
- Any remaining open questions that need product/business decisions

## Important Notes

- Be thorough but concise - requirements should be actionable
- Use concrete examples from the existing codebase
- Reference specific files, functions, and database tables
- Ensure requirements are testable and measurable
- If you need to ask multiple rounds of questions, do so - completeness is more important than speed
