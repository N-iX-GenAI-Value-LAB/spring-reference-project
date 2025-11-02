---
command: plan
description: Creates minimal implementation phases from a research document following KISS/YAGNI
creation-date: 2025-09-16 13:19 UTC+0300
last-update: 2025-09-16 13:19 UTC+0300
---

## Description

Creates minimal implementation phases from a research document, breaking down the work into logical, manageable stages that adhere to KISS and YAGNI principles.

## Parameters

- `research-file-path` (required): Full path to the research document. If not provided or invalid, the command will prompt for it.

## Validation Rules

1. Research file must exist at the specified path
2. If file doesn't exist: error out and ask user to run research command first
3. Phase descriptions limited to max 5 words, lowercase with hyphens
4. All clarifying questions must be answered before proceeding
5. User must explicitly confirm if testing phases are needed

## Execution Steps

1. **Validate Research File**
   - Check if provided path exists
   - If not exists: display error message and suggest running `/research` first
   - If path unclear: ask user for correct research file path

2. **Clarification Phase** (MANDATORY)
   - Ask if E2E testing phase is needed
   - Ask if unit testing phase is needed
   - Ask questions to ensure full comprehension of planning goals
   - Clarify priorities and constraints
   - **IMPORTANT**: If ANY aspect of the research or planning goals is unclear or ambiguous, STOP and ask the user for clarification
   - List all unclear points and ask specific questions
   - Confirm understanding before proceeding

3. **Deep Analysis Phase** (THINK HARD)
   - **Think hard** about the phase breakdown before creating phases
   - Read and parse research findings
   - Identify key implementation areas
   - **Consult FILE_DEPENDENCY_MAP.md** to understand typical file dependencies for planned changes
   - Determine logical phase boundaries
   - Apply KISS and YAGNI principles to phase design
   - Verify the phase breakdown is logical, minimal, and complete

4. **Create Phase Structure**
   - Create folder: `docs/{topic}/phases/`
   - Generate phase files following naming convention
   - Create index file for phase overview

5. **Generate Consolidated Documentation**
   - Create single index.md with all phases inline
   - Generate phase-status.json for programmatic tracking
   - Include actual codebase file references
   - Limit to maximum 3-4 phases to maintain focus
   - Add testing phases only if explicitly requested
   - Verify total files created does not exceed limit (max 2 files)

## Output Specification

### File Structure
```
docs/{topic}/phases/
├── index.md              # Consolidated phase documentation with all phases
└── phase-status.json     # Programmatic tracking of phase completion
```

**Note**: Individual phase files are NOT created. All phase information is consolidated in index.md to reduce file proliferation.

### Index File Template (index.md)
```markdown
# Implementation Phases

**Created**: {timestamp from date command}
**Research**: [{research file name}](../research-{topic}.md)
**Status Tracking**: [phase-status.json](./phase-status.json)

## Phase Overview

### Phase 1: {Description}
- [ ] Not Started
- **Objective**: {Clear statement of what this phase accomplishes}
- **Scope**: {What is included and explicitly what is NOT included}
- **Deliverables**:
  - {Specific, measurable deliverable 1}
  - {Specific, measurable deliverable 2}
- **Success Criteria**: {How to verify this phase is complete}
- **Affected Files**:
  - `src/api/routes/*.ts` - API endpoints
  - `src/lib/database/*.ts` - Database schemas
  - (Consult [FILE_DEPENDENCY_MAP.md](../../FILE_DEPENDENCY_MAP.md) for complete file list)
- **Dependencies**: None

### Phase 2: {Description}
- [ ] Not Started
- **Objective**: {Clear statement of what this phase accomplishes}
- **Scope**: {What is included and explicitly what is NOT included}
- **Deliverables**:
  - {Specific, measurable deliverable 1}
  - {Specific, measurable deliverable 2}
- **Success Criteria**: {How to verify this phase is complete}
- **Affected Files**:
  - `src/components/*.tsx` - React components
  - `src/hooks/*.ts` - Custom hooks
- **Dependencies**: Phase 1

### Phase 3: {Description} (if needed)
- [ ] Not Started
- **Objective**: {Clear statement of what this phase accomplishes}
- **Scope**: {What is included and explicitly what is NOT included}
- **Deliverables**:
  - {Specific, measurable deliverable 1}
  - {Specific, measurable deliverable 2}
- **Success Criteria**: {How to verify this phase is complete}
- **Affected Files**:
  - `tests/**/*.test.ts` - Test files
  - `docs/*.md` - Documentation
- **Dependencies**: Phase 1, Phase 2
```

### Phase Status File Template (phase-status.json)
```json
{
  "topic": "{topic}",
  "created": "{ISO timestamp}",
  "lastUpdated": "{ISO timestamp}",
  "phases": [
    {
      "number": 1,
      "name": "{description}",
      "status": "not_started",
      "startedAt": null,
      "completedAt": null,
      "tasksTotal": 0,
      "tasksCompleted": 0,
      "implementationNotes": "",
      "relatedCommits": [],
      "relatedPRs": []
    },
    {
      "number": 2,
      "name": "{description}",
      "status": "not_started",
      "startedAt": null,
      "completedAt": null,
      "tasksTotal": 0,
      "tasksCompleted": 0,
      "implementationNotes": "",
      "relatedCommits": [],
      "relatedPRs": []
    }
  ],
  "totalFiles": 2,
  "maxFilesAllowed": 5
}
```

**Status Values**: `not_started`, `in_progress`, `completed`, `blocked`, `archived`

### Error Handling
- Research file not found:
  ```markdown
  ❌ **Error**: Research file not found at `{path}`

  Please run the research command first:
  `/research <topic>`

  Or provide a valid research file path.
  ```
- Invalid path: Request correct path with example
- Existing phases: Ask if should continue or start fresh

### Success Criteria
- Single consolidated index.md file created with all phases
- Phase-status.json created for programmatic tracking
- Maximum 2 files created (reduced from previous 4-5 files)
- Phases include references to actual codebase files
- Phases follow KISS/YAGNI principles
- Testing phases included only if explicitly requested

### File Creation Guardrails
- **Maximum files**: 2 (index.md + phase-status.json)
- **Confirmation required**: If existing phases found, ask before overwriting
- **Validation**: Check for orphaned phase files from previous runs
- **Cleanup reminder**: Suggest archiving completed phases older than 30 days