# Learnings System

Learnings optimize context understanding across tasks. Silent operations - no user output unless error occurs.

---

## Step 1b: Load Relevant Learnings (SILENT)

**After Step 1, before implementation**

1. **Path**: `docs/{topic}/learnings.json`

2. **Load/Create**:
   - If missing: create default structure:
     ```json
     {
       "featureName": "{from path}",
       "created": "{ISO timestamp}",
       "lastUpdated": "{ISO timestamp}",
       "globalLearnings": [],
       "phases": []
     }
     ```
   - If corrupted: warn "⚠️ Warning: learnings.json is corrupted. Skipping learnings load." and continue

3. **Extract** (max 5 total):
   - Priority 1: Previous task learnings from same phase (completed tasks only)
   - Priority 2: Global learnings (fill remaining slots)

4. **Format and inject at Step 4**:
   ```markdown
   ## CONTEXT: Project Learnings

   Use these insights to maintain consistency and avoid repeating mistakes.

   ### Phase {N} Task Learnings
   - {learning}

   ### Global Project Learnings
   - {learning}
   ```

5. **Behavior**: Non-blocking. Continue task even if load fails.

---

## Step 6b: Extract Learnings (KNOWLEDGE HYDRATION)

**After Step 6 (task documentation)**

**Extraction Prompt**:
```
Extract 5-10 key learnings from this task:

1. Technical decisions and rationale
2. Gotchas, edge cases, unexpected issues
3. Codebase patterns, conventions, architecture
4. Assumptions validated/invalidated
5. What worked/didn't work

Each learning:
- 1-2 sentences (guideline: 10-200 chars)
- Reference specific files/code/elements
- Actionable and context-rich
- Project-specific (not generic advice)
```

**Guidelines**:
- Target: 7-8 learnings (range: 5-10)
- Tone: Imperative/declarative ("Use X for Y", "Z requires W")

**Examples**:

✅ Good:
- "implement-task.md Step 1b creates default learnings.json if missing - follow Phase 1 format"
- "Priority-based selection: previous task learnings first, then global"
- "Silent operations: no user output unless error"

❌ Bad:
- "Always write good code" (generic)
- "Functions should be small" (not project-specific)
- "I modified the file and it worked" (no insight)

---

## Step 6c: Deduplicate Learnings (AUTOMATED)

**After Step 6b**

**Process**:

1. **Load existing** task learnings from `phases[N].tasks[X.Y].learnings` (or empty if not exists)

2. **Two-Tier Deduplication** (check against existing task learnings only):

   **Tier 1: Exact Match**
   - Normalize: lowercase, trim, remove extra spaces
   - If exact match: SKIP new learning

   **Tier 2: High Similarity (0.85 threshold)**
   - Algorithm:
     ```
     1. Split into word sets (lowercase)
     2. similarity = intersection / union
     3. If similarity ≥ 0.85: keep longer (more specific)
     ```
   - Examples:
     - "Step 1b creates learnings.json" vs "Step 1b creates learnings.json if missing" → 4/6=0.67 → KEEP BOTH
     - "Claude Code uses Step 1b for loading" vs "Claude Code uses Step 1b for loading context" → 7/8=0.875 → CONSOLIDATE (keep longer)

3. **Combine**: existing + new (that passed deduplication)

4. **Enforce max 10**:
   - If >10: remove oldest first (FIFO)
   - Keep last 10

**Fully automated** - no user prompts.

---

## Step 6d: Save Learnings (PERSISTENCE)

**After Step 6c**

1. **Load** `docs/{topic}/learnings.json`:
   - If missing: create default structure
   - If corrupted: skip save, warn user, continue task

2. **Update**:
   - Navigate to `phases[phaseN].tasks[taskX.Y]`
   - Create phase/task if not exists
   - Replace task's learnings array with deduplicated array
   - Preserve all other data

3. **Timestamp**:
   - Update `lastUpdated`: `date -u +"%Y-%m-%dT%H:%M:%SZ"`
   - Preserve `created`

4. **Write**:
   - JSON with 2-space indentation
   - Atomic write (temp file, then rename)
   - If fails: warn user, continue task

**Error handling**: Non-blocking. Task completes even if save fails.

**Preservation**: Keep all existing phases, tasks, globalLearnings, metadata.

---

## JSON Schema

```json
{
  "featureName": "feature-name",
  "created": "2025-10-13T08:00:00Z",
  "lastUpdated": "2025-10-13T09:00:00Z",
  "globalLearnings": ["insight"],
  "phases": [{
    "phaseNumber": 1,
    "phaseName": "Phase Name",
    "tasks": [{
      "taskId": "1.1",
      "taskName": "Task Name",
      "learnings": ["learning 1", "learning 2"]
    }]
  }]
}
```

---

## Example

Task 1.1 completes with 3 learnings:
```json
{
  "featureName": "auth-system",
  "phases": [{
    "phaseNumber": 1,
    "phaseName": "Database Schema",
    "tasks": [{
      "taskId": "1.1",
      "taskName": "Create users table",
      "learnings": [
        "Drizzle ORM uses schema.ts for table definitions",
        "Use timestamp() not datetime() for PostgreSQL compatibility",
        "CASCADE DELETE on foreign keys prevents orphaned records"
      ]
    }]
  }]
}
```

Task 1.2 starts → Step 1b loads 3 learnings from 1.1 → injects at Step 4
