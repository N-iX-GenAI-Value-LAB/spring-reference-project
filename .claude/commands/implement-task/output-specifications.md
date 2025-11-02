# Output Specifications

Documentation formats, templates, and error patterns.

---

## Task Documentation Updates (Step 6)

### Completion Status in tasks.md

Replace:
```markdown
### Task X.Y: {task-name}
- [ ] Not Started
```

With:
```markdown
### Task X.Y: {task-name}
- [x] Completed
- **Completion date**: {timestamp from date command}
```

### Implementation Summary

Add after completion status:

```markdown
#### Implementation Summary

**Completed**: {timestamp from date command}

**Files Created**:
- `path/to/file.ext` - [Description]

**Files Modified**:
- `path/to/file.ext` - [What changed]

**Files Deleted**:
- `path/to/file.ext` - [Reason]

**Issues Encountered**:
- [Issue and resolution]

**Notes**: [Additional notes]
```

### task-status.json

```json
{
  "id": "X.Y",
  "status": "completed",
  "completedAt": "{ISO timestamp}",
  "actualHours": {hours},
  "filesModified": ["files"],
  "relatedCommits": [],
  "notes": "Summary"
}
```

---

## Error Handling Patterns

### Task Not Found

```markdown
❌ **Error**: Task not found

Task ID: {task-id}
Expected: `docs/{topic}/tasks/phase-{X}/tasks.md`

Verify:
1. Task ID format (X.Y)
2. Phase directory exists
3. tasks.md exists
4. Task ID in file
```

### Task Already Completed

```markdown
⚠️ **Task Already Completed**

Task `{task-id}: {task-name}` completed on {timestamp}

Options:
1. Re-implement (overwrites)
2. Cancel
3. Verify task

How to proceed?
```

### Dependency Not Met

```markdown
⚠️ **Blocker**: Prerequisite not completed

Task `{dependency-id}` required first.

Options:
1. Complete prerequisite
2. Proceed anyway (risky)
3. Cancel

How to proceed?
```

### Implementation Blocker

```markdown
🛑 **Blocker Encountered**

Issue: {description}
Attempted: {what tried}

Options:
1. {Resolution}
2. Skip part
3. Cancel

How to proceed?
```

---

## Phase Completion (Step 7)

**When**: Last task in phase completes

### Update index.md

Replace:
```markdown
### Phase {N}: {phase-name}
- [ ] Not Started
```

With:
```markdown
### Phase {N}: {phase-name}
- [x] Completed
- **Completion date**: {timestamp}
```

### Phase Summary

```markdown
#### Phase Implementation Summary

**Completed**: {timestamp}

**Tasks Completed**: {N} of {N}
- Task {N}.1: {name} ✓
- Task {N}.2: {name} ✓

**Files Modified/Created**: {count}
- {file list}

**Key Deliverables**:
- {deliverable}

**Notes**: {notes}
```

---

## Success Criteria Checklist

- [ ] Task completed or blockers documented
- [ ] All file changes tracked
- [ ] tasks.md updated with completion status
- [ ] task-status.json updated
- [ ] Implementation summary added
- [ ] Phase marked complete if last task
- [ ] KISS and YAGNI followed
- [ ] No hard-coded values
- [ ] `npm run build` succeeds

---

## Example: Simple Task

**Before**:
```markdown
### Task 1.2: Add validation schema
- [ ] Not Started

**Objective**: Create Zod schema

**Acceptance Criteria**:
- Validates prompt (min 1, max 5000 chars)
- Exported from validation/schemas/
```

**After**:
```markdown
### Task 1.2: Add validation schema
- [x] Completed
- **Completion date**: 2025-10-13 14:30:00 UTC
- **Estimated Time**: 1 hour
- **Actual Time**: 45 minutes

#### Implementation Summary

**Completed**: 2025-10-13 14:30:00 UTC

**Files Created**:
- `src/lib/api/conversation/validation/schemas/conversation.ts` - Zod schema

**Files Modified**: None

**Files Deleted**: None

**Issues Encountered**: None

**Notes**: Used string().min(1).max(5000) for validation.

**Objective**: Create Zod schema

**Acceptance Criteria**:
- Validates prompt (min 1, max 5000 chars) ✓
- Exported from validation/schemas/ ✓
```
