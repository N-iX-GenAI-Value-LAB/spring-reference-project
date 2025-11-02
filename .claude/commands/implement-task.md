---
command: implement-task
description: Executes implementation of a specific task and updates documentation with completion status
creation-date: 2025-09-16 13:19 UTC+0300
last-update: 2025-10-13 16:00 UTC+0300
---

## Description

Executes the implementation of a specific task from consolidated task files, following KISS and YAGNI principles. Updates task documentation with completion status and implementation details.

> **📚 Organized Documentation Available!**
>
> This command has been split into focused guides for easier navigation:
>
> - **[Learnings System](./implement-task/learnings-system.md)** - Context loading, extraction, deduplication, persistence (Steps 1b, 6b, 6c, 6d)
> - **[Implementation Workflow](./implement-task/implementation-workflow.md)** - Pre-checks, clarification, implementation, blockers (Steps 2, 3, 4, 5)
> - **[Output Specifications](./implement-task/output-specifications.md)** - Documentation templates and error patterns (Step 6)
>
> This document focuses on **overview, parameters, and execution orchestration**.

---

## Parameters

- `task-id` (required): Task identifier in format X.Y (e.g., "1.1", "2.3"). Will locate the task in the consolidated tasks.md file.
- `phase-path` (optional): Path to phase directory containing tasks.md. If not provided, will search in docs/{topic}/tasks/phase-{X}/ based on task ID.

---

## Validation Rules

1. Consolidated tasks.md file must exist in phase directory
2. Task must be found in the consolidated file and not already completed
3. All dependencies must be checked before implementation
4. User must be consulted for any blockers or unclear requirements
5. Implementation must follow KISS and YAGNI principles
6. No hard-coding of values - use configuration or environment variables
7. Task documentation must be updated upon completion or partial completion

---

## Execution Steps Overview

### Step 1: Locate and Parse Task

1. Extract phase number from task ID (X from X.Y format)
2. If phase-path provided: use it directly
3. Otherwise: search for tasks.md in docs/{topic}/tasks/phase-{X}/
4. Parse tasks.md file to locate task section by ID (Task X.Y: {name})
5. Read task requirements, objective, and acceptance criteria

### Step 1b: Load Relevant Learnings into Context (SILENT)

**See [learnings-system.md](./implement-task/learnings-system.md) for complete specification**

- Automatically load learnings.json from docs/{topic}/learnings.json
- Extract max 5 relevant learnings (priority: previous task learnings, then global)
- Format as markdown block for context injection at Step 4
- Silent operation: no user output unless error occurs
- Non-blocking: continue even if load fails

### Step 2: Pre-implementation Checklist

**See [implementation-workflow.md](./implement-task/implementation-workflow.md) for details**

- Verify all prerequisite tasks are completed (check Prerequisites section)
- Check that required dependencies are available
- Ensure understanding of Files to Modify/Create sections
- Confirm development environment is ready

### Step 3: Clarification Phase (MANDATORY)

**See [implementation-workflow.md](./implement-task/implementation-workflow.md) for details**

- **CRITICAL**: This step is MANDATORY. Do NOT skip.
- Ask questions about any unclear requirements
- Clarify implementation approach and constraints
- Confirm understanding of acceptance criteria
- Ask if tests should be generated and executed
- **Consult FILE_DEPENDENCY_MAP.md** to identify all required files for this change type
- Get approval before proceeding with implementation

### Step 4: Deep Analysis and Implementation (THINK HARD)

**See [implementation-workflow.md](./implement-task/implementation-workflow.md) for details**

- **[LEARNINGS CONTEXT INJECTION POINT]** - Learnings from Step 1b are automatically included here
- Think hard about the implementation approach before coding
- **Reference FILE_DEPENDENCY_MAP.md** to ensure all required files are modified
- Follow KISS principle: implement simplest solution that works
- Follow YAGNI principle: only implement what's needed now
- Create/modify/delete files as specified (verify against dependency map)
- Use environment variables or config files (no hard-coding)
- Handle errors gracefully
- Update rest-api-contracts-initial.yaml if API changes are involved
- Verify the implementation is correct, complete, and follows all acceptance criteria

### Step 5: Handle Blockers

**See [implementation-workflow.md](./implement-task/implementation-workflow.md) for details**

- If blocked: MANDATORY ask user how to proceed
- Document the blocker clearly
- Provide options for resolution
- Wait for user decision before continuing

### Step 6: Update Task Documentation

**See [output-specifications.md](./implement-task/output-specifications.md) for templates**

- Mark completion status in task section with timestamp
- Update task-status.json with completion details
- Add implementation summary to task section
- Document all changes made
- Note any issues or incomplete items

### Step 6b: Extract Learnings from Implementation (KNOWLEDGE HYDRATION)

**See [learnings-system.md](./implement-task/learnings-system.md) for complete specification**

- Extract 5-10 key learnings from the task implementation
- Focus on: technical decisions, gotchas, codebase patterns, assumptions, approaches
- Target: 7-8 learnings per task (guideline: 10-200 characters each)
- Be specific, actionable, and project-specific
- Output: learnings ready for deduplication

### Step 6c: Deduplicate and Consolidate Learnings (AUTOMATED)

**See [learnings-system.md](./implement-task/learnings-system.md) for complete specification**

- Check new learnings against existing task learnings only
- Two-tier deduplication: exact match (skip), high similarity 0.85+ (consolidate)
- Enforce max 10 learnings per task (FIFO if exceeded)
- Fully automated: no user prompts
- Output: deduplicated learnings array ready for save

### Step 6d: Save Learnings to learnings.json (PERSISTENCE)

**See [learnings-system.md](./implement-task/learnings-system.md) for complete specification**

- Load learnings.json (create default structure if missing)
- Update current task's learnings array with deduplicated learnings
- Update lastUpdated timestamp (ISO format)
- Preserve all other data (global learnings, other tasks, metadata)
- Non-blocking: task completes even if save fails

### Step 7: Check Phase Completion

- Check if this is the last task in the phase (compare completedTasks + 1 with totalTasks)
- If last task: update corresponding phase index.md file
- Mark phase as completed with timestamp and implementation summary
- See [output-specifications.md](./implement-task/output-specifications.md) for phase completion template

---

## Success Criteria

Task is considered successfully implemented when:

- [ ] Task implementation completed or blockers documented
- [ ] All file changes are tracked and documented
- [ ] Task section in tasks.md updated with completion status
- [ ] Task-status.json updated with completion details
- [ ] Implementation summary added to task section
- [ ] If the implemented task is the last task from that phase: the phase must be marked as completed in the index.md with summary
- [ ] KISS and YAGNI principles are followed thoroughly
- [ ] No hard-coded values in implementation
- [ ] The project build runs without errors - `npm run build`
- [ ] Learnings extracted, deduplicated, and saved to learnings.json (if save succeeds)

---

## Quick Reference: When to Use Which Document

### During Task Execution:

**Step 1-2**: Use this main file for overview

**Step 3-5**: Reference **[implementation-workflow.md](./implement-task/implementation-workflow.md)**
- Clarification questions and templates
- Implementation checklist and guidelines
- Blocker handling patterns

**Step 6**: Reference **[output-specifications.md](./implement-task/output-specifications.md)**
- Task documentation templates
- Error handling patterns
- Phase completion formats

**Steps 1b, 6b-6d**: Reference **[learnings-system.md](./implement-task/learnings-system.md)**
- Learnings loading behavior
- Extraction guidelines and quality examples
- Deduplication algorithm
- Save process and error handling

### For Understanding the System:

- **Learnings system overview**: See [learnings-system.md](./implement-task/learnings-system.md)
- **Implementation best practices**: See [implementation-workflow.md](./implement-task/implementation-workflow.md)
- **Documentation standards**: See [output-specifications.md](./implement-task/output-specifications.md)

---

## Notes

- Authentication is not implemented in MVP (uses hardcoded userId)
- Always consult FILE_DEPENDENCY_MAP.md during Step 3 and Step 4
- Learnings system is for Claude Code's internal use, NOT for user-facing display
- All learnings operations (Steps 1b, 6b, 6c, 6d) are non-blocking - task completes even if learnings fail
- Step 3 (Clarification) is MANDATORY - never skip even if task seems clear
- Use environment variables or config files - never hard-code values
- Keep rest-api-contracts-initial.yaml in sync with implementation
