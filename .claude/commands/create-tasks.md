---
command: create-tasks
description: Breaks down implementation phase into small tasks under 1 hour with max 3 files each
creation-date: 2025-09-16 13:19 UTC+0300
last-update: 2025-09-20 16:00 UTC+0300
---

## Description

Breaks down an implementation phase into specific, small tasks that follow KISS and YAGNI principles. Each task:
- must be implementable in less than 1 hour
- must modify or create at most 3 files

## Parameters

- `phase-file-path` (required): Full path to the phase document. If not provided or invalid, the command will prompt for it.

## Validation Rules

1. Phase file must exist at the specified path
2. If tasks already exist for the phase: ask whether to overwrite
3. Task numbering follows format: task-{phase}.{number} (e.g., task-1.1, task-1.2)
4. Task descriptions in filenames should be concise and descriptive
5. All clarifying questions must be answered before proceeding

## Execution Steps

1. **Validate Phase File**
   - Check if provided path exists
   - If not exists: display error and ask for valid phase file path
   - Extract phase number from filename

2. **Check Existing Tasks**
   - Look for existing tasks in `docs/{topic}/tasks/phase-{N}/`
   - If tasks exist: ask user whether to overwrite or append
   - Record user's preference

3. **Clarification Phase** (MANDATORY)
   - Ask questions to ensure full comprehension of task creation goals
   - Clarify granularity and specific requirements
   - Confirm if testing requirements should be included
   - **IMPORTANT**: If ANY requirement is unclear or ambiguous, STOP and ask the user for clarification
   - List all unclear points and ask specific questions
   - **If previous phases exist**: Ask if specific learnings/patterns should be emphasized or avoided in current phase
   - Verify understanding before proceeding
   - Explain task constraints: < 1 hour implementation (target 30-45 min), max 3 files per task (target 1-2 files)

4. **Deep Analysis Phase** (THINK HARD)
   - **FIRST: Load learnings.json** to understand existing context, patterns, and proven approaches from previous phases
   - **Analyze learnings** to identify **project-specific patterns** (prioritize codebase interactions over generic library features):
     - **Project architecture patterns** that worked well (e.g., how TestFixturesService coordinates with ApiClient, TestContext extension patterns)
     - **Codebase-specific pitfalls** to avoid (e.g., incorrect deletion order for this project's FK constraints, cleanup delegation patterns)
     - **Successful task sizing** from previous phases (target 30-45 minutes, 1-2 files per task)
     - **Project design decisions** and their rationale (e.g., optional fields for backward compatibility in this codebase)
     - **Integration patterns between project components** (e.g., lifecycle hooks + TestContext coordination)
     - **Project-specific optimizations** (e.g., document reuse strategy, environment variable usage patterns)
   - **Apply learnings** to current phase:
     - Reuse successful project-specific patterns and approaches
     - Structure tasks to follow proven sequencing from completed phases
     - Avoid repeating mistakes documented in previous phases of this project
     - Maintain consistency with established project conventions
     - Focus on project file interactions, not generic library documentation
   - Read phase objectives and deliverables
   - **Consult FILE_DEPENDENCY_MAP.md** to understand which files are affected by each change type
   - Identify discrete work units that can be completed in < 1 hour (target: 30-45 minutes)
   - Ensure each task modifies or creates at most 3 files (target: 1-2 files)
   - Reject or split tasks that exceed these constraints
   - Determine task dependencies and sequence
   - Cross-reference with dependency map to ensure completeness
   - Verify the task breakdown is logical, complete, and adheres to KISS/YAGNI principles
   - **Think hard** about the task breakdown before proceeding

5. **Create Task Structure**
   - Create folder: `docs/{topic}/tasks/phase-{N}/`
   - Generate single consolidated tasks.md file
   - Create task-status.json for tracking
   - **Update learnings.json** with phase and task structure
   - Ensure logical progression and dependencies
   - Integrate with TodoWrite tool if possible
   - Verify total files does not exceed limit (max 2 files)

## Output Specification

### File Structure
```
docs/{topic}/tasks/
└── phase-{N}/
    ├── tasks.md           # Consolidated task list for the phase
    └── task-status.json   # Programmatic tracking of task completion
```

**Note**: Individual task files are NOT created. All tasks are consolidated in a single tasks.md file per phase to reduce file proliferation.

### Consolidated Tasks File Template (tasks.md)
```markdown
# Phase {N} Tasks

**Created**: {timestamp from date command}
**Phase**: Phase {N}: {Phase Description}
**Status Tracking**: [task-status.json](./task-status.json)
**Total Tasks**: {count}
**Completed**: 0/{count}

## Task Checklist

### Task {N}.1: {Description}
- [ ] Not Started
- **Estimated Time**: < 1 hour
- **Objective**: {Clear statement of what this task accomplishes}
- **Files to Modify/Create** (max 3):
  - `src/api/routes/example.ts` - Add endpoint
  - `src/lib/validators/example.ts` - Add validation
- **Acceptance Criteria**:
  - Endpoint responds with correct status code
  - Validation catches invalid inputs
  - Tests pass

### Task {N}.2: {Description}
- [ ] Not Started
- **Estimated Time**: < 1 hour
- **Prerequisites**: Task {N}.1
- **Objective**: {Clear statement of what this task accomplishes}
- **Files to Modify/Create** (max 3):
  - `src/components/Example.tsx` - Update UI
  - `src/hooks/useExample.ts` - Add logic
- **Acceptance Criteria**:
  - UI displays correctly
  - State updates properly
  - No console errors

### Task {N}.3: {Description}
- [ ] Not Started
- **Estimated Time**: < 1 hour
- **Prerequisites**: Task {N}.2
- **Objective**: {Clear statement of what this task accomplishes}
- **Files to Create** (max 3):
  - `tests/example.test.ts` - Add tests
- **Acceptance Criteria**:
  - All tests pass
  - Coverage above 80%

## Task Validation Rules

**MANDATORY CONSTRAINTS:**
- ❌ **REJECT** tasks that require > 1 hour of implementation
- ❌ **REJECT** tasks that modify or create > 3 files
- ✅ **SPLIT** large tasks into smaller sub-tasks that meet constraints
- ✅ **PREFER** tasks that take 30-45 minutes and touch 1-2 files

**Examples of tasks to REJECT/SPLIT:**
- "Implement complete user authentication system" → Split into: login form, validation, API endpoint, etc.
- "Refactor entire API layer" → Split by specific endpoints or modules
- "Add comprehensive testing" → Split by component/feature area

## Implementation Notes

### Leveraging Previous Phase Learnings
When creating tasks for Phase N, always review learnings from Phases 1 through N-1.

**IMPORTANT**: Focus on **project-specific patterns**, not generic library features.

**Project Architecture Patterns to Reuse**:
{List successful patterns from previous phases, e.g.:}
- How TestFixturesService coordinates with ApiClient (dependency injection pattern in this project)
- How TestContext extends with optional fields for backward compatibility (project extensibility pattern)
- How lifecycle hooks delegate cleanup responsibilities (project module coordination)
- Document reuse strategy to avoid unstructured.io reprocessing (project optimization pattern)

**Proven Task Sequencing** (Project-Specific):
{List effective task ordering patterns from completed phases, e.g.:}
- Interfaces/service scaffolding → Implementation methods → Integration → Verification
- Direct DB methods before API integration methods
- Cleanup infrastructure before lifecycle integration
- Verification as final task with real test execution

**Common Pitfalls to Avoid** (From This Codebase):
{List known issues from previous phases in this project, e.g.:}
- Deletion order violates FK constraints (documents before files, ratings/comments before conversations)
- Missing error handling in cleanup paths masks scenario failures
- Premature tracking (e.g., versionId) violates YAGNI for fixture methods
- Type assertions needed for forward-compatible context fields

**Integration Points** (Project-Specific):
{List established integration patterns in this codebase, e.g.:}
- TestContext extension pattern with optional fields
- Lifecycle hooks coordinate setup/teardown via context.fixtures
- ApiClient singleton shared across all test contexts
- Fixture tracking delegation (TestFixturesService owns tracking, not TestContext)

### Framework-Specific Guidance
- {Any framework-specific notes}
- **Reference**: Consult [FILE_DEPENDENCY_MAP.md](../../FILE_DEPENDENCY_MAP.md) for change type patterns and required files
- **Reference**: Consult [learnings.json](../../learnings.json) for proven patterns and approaches from completed phases

## Testing Requirements
[Only included if user requested testing in planning phase]
- [ ] Unit tests for all new functions
- [ ] Integration tests for API endpoints
- [ ] E2E tests for user flows
```

### Task Status File Template (task-status.json)
```json
{
  "phaseNumber": {N},
  "phaseName": "{Phase Description}",
  "created": "{ISO timestamp}",
  "lastUpdated": "{ISO timestamp}",
  "tasks": [
    {
      "id": "{N}.1",
      "name": "{description}",
      "status": "not_started",
      "completedAt": null,
      "notes": "",
      "blockers": [],
      "filesModified": []
    },
    {
      "id": "{N}.2",
      "name": "{description}",
      "status": "not_started",
      "completedAt": null,
      "notes": "",
      "blockers": [],
      "filesModified": []
    }
  ],
  "totalTasks": {count},
  "completedTasks": 0,
  "totalChangedAndCreatedFiles": {countOfChangedAndCreatedFiles},
  "maxFilesAllowedPerTask": 3,
  "todoWriteIntegration": true
}
```

**Status Values**: `not_started`, `in_progress`, `completed`, `blocked`, `skipped`

### Update learnings.json

**Purpose**: Context continuity for Claude Code (internal use, not user-facing). Learnings optimize Claude's context understanding when starting tasks with empty context window.

**Action**: Append new phase structure with task placeholders

**Behavior**:
- Load `docs/{topic}/learnings.json`
- If missing: create with warning, initialize empty structure
- If phase exists: preserve existing learnings, update task names only
- Append new phase with empty learnings arrays for each task
- Update `lastUpdated` timestamp

**Learning Priorities** (in order of importance):
1. **Project Files & Interactions** - Patterns specific to this codebase's architecture and component interactions
2. **Project Dependencies** - How dependencies are used within this project's context
3. ~~Generic Library Features~~ - Avoid documenting well-known library APIs

**What Makes Good Learnings** (examples from completed phases):

**HIGH VALUE - Project-Specific Patterns**:
- **File Interactions**: "TestFixturesService uses dependency injection with ApiClient for flexibility" (how project components interact)
- **Codebase Architecture**: "Lifecycle hooks integrate with TestContext via optional fields for backward compatibility" (project design patterns)
- **Project-Specific Logic**: "Document reuse queries files table by filename to skip unstructured.io processing" (business logic specific to this project)
- **Integration Points**: "Cleanup delegation pattern: resetContext() doesn't call cleanup, lifecycle hooks handle it" (how modules coordinate)
- **Database Design Choices**: "Deletion order critical: documents before files, ratings/comments before conversations to avoid FK violations" (project schema relationships)
- **Error Handling Strategy**: "Try-catch around cleanup prevents masking scenario errors with cleanup failures" (project error handling conventions)
- **Testing Strategy**: "Database state comparison before/after is ultimate proof of idempotence" (project testing approach)
- **Configuration Patterns**: "Process.env.PERFORM_EXT_REQUESTS determines test mode (LIVE vs CACHED)" (project environment usage)

**MEDIUM VALUE - Dependency Usage in Project Context**:
- **Project Usage Pattern**: "ApiClient singleton shared across test contexts reduces memory overhead" (how project uses dependencies, not generic singleton pattern)
- **Contextual Library Use**: "CASCADE DELETE in conversations table auto-deletes messages and versions per schema design" (how project leverages DB features)

**LOW VALUE - Avoid Generic Library Documentation**:
- ❌ "Drizzle .returning() method extracts inserted record IDs efficiently" (generic Drizzle API, not project-specific)
- ❌ "Optional fields in TypeScript interfaces enable backward compatibility" (generic TypeScript feature)
- ❌ "inArray operator required for batch deleting multiple records" (generic Drizzle API)
- ❌ "logger.debug() for initialization and tracking" (generic logger usage)

**What to Avoid**:
- Generic library/framework API documentation (already exists in official docs)
- Restating obvious code behavior without explaining project-specific "why"
- Implementation details without project context or rationale
- Language features unrelated to project patterns

**Example Update**:
```json
{
  "featureName": "existing-feature",
  "created": "2025-10-13T05:29:28Z",
  "lastUpdated": "{new-timestamp}",
  "globalLearnings": [],
  "phases": [
    {
      "phaseId": "1",
      "phaseName": "Existing Phase",
      "tasks": [
        {
          "taskId": "1.1",
          "taskName": "Existing task",
          "learnings": ["preserved learning"]
        }
      ]
    },
    {
      "phaseId": "2",
      "phaseName": "New Phase Name",
      "tasks": [
        {
          "taskId": "2.1",
          "taskName": "Task 2.1 name",
          "learnings": []
        },
        {
          "taskId": "2.2",
          "taskName": "Task 2.2 name",
          "learnings": []
        }
      ]
    }
  ]
}
```

**Error Handling**:
- Missing learnings.json: Display warning, create file with empty structure
- Corrupted JSON: Display error with recovery options (reset, manual fix, skip)
- Phase already exists: Preserve existing learnings, only update task structure if needed

### Error Handling
- Phase file not found:
  ```markdown
  ❌ **Error**: Phase file not found at `{path}`

  Please provide a valid phase file path.
  Available phases should be in: `docs/{topic}/phases/`
  ```

- Existing tasks found:
  ```markdown
  ⚠️ **Warning**: Tasks already exist for this phase

  Found {N} existing tasks in `docs/{topic}/tasks/phase-{N}/`

  Would you like to:
  1. Append new tasks to the existing tasks
  2. Overwrite the existing tasks

  Please specify your choice.
  ```

### Success Criteria
- Single consolidated tasks.md file created per phase
- Task-status.json created for programmatic tracking
- learnings.json updated with phase and task structure
- **Learnings from previous phases reviewed and applied** to task structure
- Each task sized for less than 1 hour of work (target: 30-45 minutes)
- Each task modifies or creates at most 3 files (target: 1-2 files)
- Single focused objective per task, adhering to SRP
- Tasks that exceed constraints are rejected or split
- Tasks include references to actual codebase files
- Dependencies clearly documented
- Tasks follow logical progression
- KISS and YAGNI principles applied
- All tasks follow MVP principles with simple, direct implementations and no unnecessary abstractions
- Integration with TodoWrite tool documented
- **Implementation Notes section populated** with relevant patterns, pitfalls, and integration points from learnings.json
- Task breakdown demonstrates awareness of proven approaches from completed phases

### File Creation Guardrails
- **Maximum files per phase**: 2 (tasks.md + task-status.json)
- **Task limit per phase**: 10 tasks maximum
- **Time constraint**: Each task must be completable in < 1 hour (target: 45 minutes)
- **File constraint**: Each task may modify or create at most 3 files
- **Task validation**: Reject or split tasks that exceed time or file constraints
- **Confirmation required**: If tasks already exist, ask before overwriting
- **Validation**: Verify referenced codebase files exist
- **TodoWrite sync**: Offer to populate TodoWrite with tasks
- **Cleanup reminder**: Suggest removing old task files after completion