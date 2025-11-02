---
description: Analyze current implementation against defined requirements
---

# Analyze Requirements

You are tasked with analyzing the current codebase implementation against defined requirements.

## Input

The user will provide the path to a requirements document (typically from `docs/requirements/`) or you should ask which requirements document to analyze.

## Your Process

1. **Read Requirements Document**
   - Read the requirements document from `docs/requirements/`
   - Extract all key requirements:
     - API contracts (endpoints, request/response formats)
     - Database schema changes
     - Business logic requirements
     - Performance requirements
     - Security requirements
     - Error handling requirements

2. **Analyze Current Implementation**
   - Use the Task tool with subagent_type=Explore to find relevant implementation files
   - For each requirement category, examine:
     - **API Routes**: Check `src/app/api/v1/` for endpoint implementations
     - **Services**: Check `src/lib/domain/` and `src/lib/data/` for business logic
     - **Database Schema**: Check `src/lib/core/db/schema.ts` for table definitions
     - **Migrations**: Check `drizzle/migrations/` for schema changes
     - **Validation**: Check for Zod schemas and validation logic
     - **Error Handling**: Check error responses and logging
     - **Tests**: Check for existing test coverage

3. **Compare Implementation vs Requirements**
   - For each requirement, determine:
     - ✅ **Fully Implemented**: Code matches requirement exactly
     - ⚠️ **Partially Implemented**: Code exists but doesn't fully meet requirement
     - ❌ **Not Implemented**: Requirement has no corresponding implementation
     - 🔄 **Needs Refactoring**: Implementation exists but needs changes to meet requirement
     - ⚡ **Implementation Differs**: Code behaves differently than specified

4. **Create Analysis Document**
   - Create a markdown file in `docs/requirements/analysis/` with the same base name as the requirements file (e.g., `caching-requirements.md` → `analysis/caching-analysis.md`)
   - Use the following structure:

```markdown
# [Feature Name] Implementation Analysis

**Requirements Document**: `docs/requirements/[filename].md`
**Analysis Date**: [Current Date]
**Analysis Status**: [Complete/Partial/In Progress]

## Executive Summary

Brief overview of the analysis:
- Overall completion percentage
- Major gaps or concerns
- Critical issues requiring immediate attention
- Recommended next steps

---

## Current State vs Desired State

### API Contracts

#### Endpoint: `[METHOD] /path`
**Status**: ✅ Fully Implemented | ⚠️ Partially Implemented | ❌ Not Implemented | 🔄 Needs Refactoring | ⚡ Differs

**Current Implementation**:
- File: `src/app/api/v1/path/route.ts`
- Current behavior: [description]
- Request handling: [description]
- Response format: [description]
- Status codes: [list]

**Desired State** (from requirements):
- Expected behavior: [description]
- Required request format: [description]
- Required response format: [description]
- Required status codes: [list]

**Gap Analysis**:
- Missing: [list what's missing]
- Incorrect: [list what's wrong]
- Extra: [list what's implemented but not required]

**Files to Modify**:
- `file/path.ts:line` - [what needs to change]

---

### Database Schema

#### Table: `table_name`
**Status**: ✅ Fully Implemented | ⚠️ Partially Implemented | ❌ Not Implemented | 🔄 Needs Refactoring

**Current Schema** (`src/lib/core/db/schema.ts`):
```typescript
// Current table definition
```

**Desired Schema** (from requirements):
```typescript
// Required table definition
```

**Gap Analysis**:
- Missing columns: [list]
- Missing indexes: [list]
- Missing constraints: [list]
- Migration needed: [yes/no]

**Migration Strategy**:
- Create migration: `drizzle/migrations/XXXX_description.sql`
- Backwards compatibility: [approach]
- Data transformation: [if needed]

---

### Business Logic

#### Requirement: [Specific Requirement Statement]
**Status**: ✅ Fully Implemented | ⚠️ Partially Implemented | ❌ Not Implemented | 🔄 Needs Refactoring

**Current Implementation**:
- Service: `src/lib/domain/service.ts:functionName`
- Current behavior: [description]
- Code reference:
  ```typescript
  // Relevant code snippet
  ```

**Desired Behavior** (from requirements):
- Expected behavior: [description]

**Gap Analysis**:
- What's missing: [description]
- What needs to change: [description]

**Files to Modify**:
- `file/path.ts:line` - [what needs to change]

---

### Performance Requirements

#### Requirement: [Specific Performance Requirement]
**Status**: ✅ Met | ⚠️ Partially Met | ❌ Not Met | 🔍 Needs Measurement

**Current Implementation**:
- Caching: [yes/no, details]
- Indexes: [list relevant indexes]
- Query optimization: [description]

**Desired Performance** (from requirements):
- Caching strategy: [description]
- Index requirements: [list]
- Response time targets: [numbers]

**Gap Analysis**:
- Performance issues: [list]
- Optimization opportunities: [list]

---

### Security & Error Handling

#### Security Requirement: [Specific Requirement]
**Status**: ✅ Implemented | ⚠️ Partially Implemented | ❌ Not Implemented

**Current Implementation**: [description]
**Desired State**: [description]
**Gap**: [description]

#### Error Handling: [Specific Scenario]
**Status**: ✅ Implemented | ⚠️ Partially Implemented | ❌ Not Implemented

**Current Implementation**: [description]
**Desired State**: [description]
**Gap**: [description]

---

## Testing Coverage

### Unit Tests
**Status**: ✅ Good Coverage | ⚠️ Partial Coverage | ❌ No Coverage

**Existing Tests**:
- [list test files and what they cover]

**Missing Tests**:
- [list test scenarios from requirements that aren't covered]

### Integration Tests
**Status**: ✅ Good Coverage | ⚠️ Partial Coverage | ❌ No Coverage

**Existing Tests**:
- [list test files and what they cover]

**Missing Tests**:
- [list test scenarios from requirements that aren't covered]

---

## Implementation Roadmap

### Phase 1: Critical Gaps (Must Have)
Priority fixes needed for basic functionality:
1. [ ] **[Task]** - `file/path.ts` - [description]
2. [ ] **[Task]** - `file/path.ts` - [description]

### Phase 2: Important Gaps (Should Have)
Important for complete feature:
1. [ ] **[Task]** - `file/path.ts` - [description]
2. [ ] **[Task]** - `file/path.ts` - [description]

### Phase 3: Nice to Have (Could Have)
Enhancements and optimizations:
1. [ ] **[Task]** - `file/path.ts` - [description]
2. [ ] **[Task]** - `file/path.ts` - [description]

### Out of Scope
Items explicitly not part of current requirements:
- [list]

---

## Dependencies & Blockers

### Internal Dependencies
- Services: [list service dependencies]
- Database: [list table dependencies]
- Shared utilities: [list]

### External Dependencies
- Third-party libraries: [list]
- Environment variables: [list]

### Blockers
- [list any blockers preventing implementation]

---

## Recommendations

1. **Immediate Actions**:
   - [recommendation 1]
   - [recommendation 2]

2. **Architecture Considerations**:
   - [recommendation 1]
   - [recommendation 2]

3. **Performance Optimizations**:
   - [recommendation 1]
   - [recommendation 2]

4. **Technical Debt**:
   - [items to address]

---

## Risk Assessment

### High Risk
- [risk 1]: [mitigation strategy]

### Medium Risk
- [risk 1]: [mitigation strategy]

### Low Risk
- [risk 1]: [mitigation strategy]

---

## Appendix

### File Index
Complete list of files examined:
- `src/app/api/v1/path/route.ts` - [description]
- `src/lib/domain/service.ts` - [description]
- ...

### Code References
Key code snippets and line numbers for quick navigation.

### Related Documentation
- API Documentation: [link]
- Database Schema: [link]
- Related Requirements: [link]
```

## Output

After creating the analysis document, inform the user:
- The path to the created analysis file
- Executive summary of findings
- Critical gaps requiring immediate attention
- Recommended next steps

## Important Notes

- Be thorough in examining the codebase - use the Task/Explore agent for comprehensive searches
- Provide specific file paths and line numbers for all findings
- Include code snippets to illustrate gaps or issues
- Prioritize findings by impact and effort
- Link to actual code using file:line references (e.g., `src/app/api/route.ts:42`)
- If implementation differs significantly from requirements, note whether requirements or code should change
