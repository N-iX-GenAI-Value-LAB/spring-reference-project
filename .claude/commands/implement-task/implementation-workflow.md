# Implementation Workflow

Core implementation workflow for tasks (Steps 2-5).

---

## Step 2: Pre-implementation Checklist

1. **Prerequisite tasks**: Check Prerequisites section, confirm all dependencies completed
2. **Dependencies**: Verify npm packages, environment variables, database, external services
3. **Files**: Review Files to Modify/Create, locate in codebase, understand current state
4. **Environment**: `npm run build` succeeds, `npm run dev` starts, database accessible

If any check fails → Step 5 (blockers)

---

## Step 3: Clarification Phase (MANDATORY)

**CRITICAL**: Do NOT skip. If ANY aspect unclear, STOP and ask.

**Required Actions**:

1. **Unclear requirements**: List ALL unclear points, clarify acceptance criteria, edge cases, data formats
2. **Implementation approach**: Discuss technical approach, architectural patterns, performance concerns, error handling
3. **Acceptance criteria**: Restate in your own words, confirm understanding
4. **Tests**: Ask if tests should be generated
5. **FILE_DEPENDENCY_MAP.md** (CRITICAL): Identify ALL required files for this change type, cross-reference with task's file list, ask if discrepancy found
6. **Get approval**: Present approach, list files, wait for confirmation

**Example clarification**:
```
❓ **Clarification Needed**

Task adds "priority" field to conversations. Need clarification:

1. Valid values? (low/medium/high or 1-5?)
2. Required or optional?
3. Default for existing conversations?
4. Affects ordering in GET requests?
```

**Example FILE_DEPENDENCY_MAP.md check**:
```
📋 **File Dependency Check**

FILE_DEPENDENCY_MAP.md says "Add New Database Field" requires:
✓ Database Schema, Migration, Route Handlers, API Contract, Validation Schema

Task only lists: Database Schema, Route Handlers

Should I also update validation schema and API contract?
```

---

## Step 4: Deep Analysis and Implementation (THINK HARD)

**[LEARNINGS CONTEXT INJECTION POINT]** - Learnings from Step 1b injected here (see learnings-system.md)

**Implementation**:

1. **Think first**: Consider multiple approaches, select simplest, identify edge cases, plan error handling
2. **Check FILE_DEPENDENCY_MAP.md**: Verify all required files, use recommended utilities (`validateRouteId()`, `parseAndValidate()`)
3. **KISS**: Simplest solution, avoid over-engineering, clear variable names, explicit code
4. **YAGNI**: Only implement what's needed now, no extra features, no premature abstractions
5. **Files**: Edit (not rewrite) existing files, Write for new files only
6. **No hard-coding**: Use `process.env` or config files
7. **Error handling**: Appropriate status codes, clear messages, use utilities from `src/lib/core/http/errors.ts`
8. **API contract**: Update `rest-api-contracts-initial.yaml` if API changes
9. **Verify**: Check acceptance criteria, test manually, `npm run build`, `npm run format`

**Checklist**:
- [ ] Simplest approach selected
- [ ] FILE_DEPENDENCY_MAP.md consulted
- [ ] KISS and YAGNI followed
- [ ] No hard-coded values
- [ ] Proper error handling
- [ ] API contract updated
- [ ] All acceptance criteria met
- [ ] Builds without errors
- [ ] Formatted and linted

If blocked → Step 5

---

## Step 5: Handle Blockers

**When blocker encountered**:

1. **STOP immediately** - do NOT work around or guess
2. **Document clearly**: What attempting, what blocked, error messages
3. **Provide options**: List 2-4 resolution options with pros/cons
4. **Wait for user decision** - do NOT decide yourself

**Templates**:

**Dependency Not Met**:
```markdown
⚠️ **Blocker**: Prerequisite task not completed

Task `{dependency-id}` must be completed first.

Options:
1. Complete prerequisite task
2. Proceed anyway (may cause issues)
3. Cancel task

How to proceed?
```

**Technical/Implementation Blocker**:
```markdown
🛑 **Blocker Encountered**

Issue: {description}
Attempted: {what tried}

Options:
1. {Resolution 1}
2. {Resolution 2}
3. Skip this part
4. Cancel

How to proceed?
```

---

## Workflow Flow

```
Step 2: Checklist → pass → Step 3 | fail → Step 5
Step 3: Clarify → clear → Step 4 | unclear → ask → repeat
Step 4: Implement → done → Step 6 | blocked → Step 5
Step 5: Blocker → user resolves → return to origin step
```
