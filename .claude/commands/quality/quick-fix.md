---
command: quality:quick-fix
description: Quick implementation of small fixes without heavy workflow
---

## Description

Implements small bug fixes, typos, or simple improvements without the overhead of the full research/plan/tasks workflow. Designed for changes that affect 1-3 files.

## Parameters

- `description` (required): Brief description of what needs to be fixed (e.g., "Fix null check in document upload")
- `--test` (optional): Run tests after implementation (default: false)
- `--commit` (optional): Create git commit after successful implementation (default: false)

## Validation Rules

1. Description must be provided or user will be prompted
2. Fix must affect 3 or fewer files (enforced during implementation)
3. If change affects >3 files, suggest using full workflow instead
4. All clarifying questions must be answered before proceeding
5. Implementation must follow KISS and YAGNI principles

## Execution Steps

1. **Accept or Request Description**
   - If description provided: proceed with clarification
   - If not provided: ask user to describe the fix needed
   - Validate that fix is "small" (if user says "refactor entire API", push back)

2. **Clarification Phase** (MANDATORY)
   - Ask questions to understand the exact issue
   - Ask which files likely need changes
   - Ask if tests should be run after
   - Ask if commit should be created
   - **IMPORTANT**: If ANY aspect is unclear, STOP and ask for clarification
   - Confirm this is truly a small fix (≤3 files)
   - If too large: suggest using full workflow

3. **Locate Affected Files**
   - Use Glob/Grep to find relevant files mentioned in description
   - Read affected files to understand current implementation
   - Verify the issue exists as described
   - Identify exact lines that need changes
   - Count files to be modified (enforce ≤3 file limit)

4. **Implementation** (KISS/YAGNI)
   - Implement the fix using simplest approach
   - Follow existing code patterns in the file
   - No refactoring beyond the fix itself
   - No "while I'm here" improvements
   - Handle errors gracefully

5. **Verification**
   - Verify the fix is complete
   - Read modified files to confirm changes
   - If `--test` flag: run `mvn test` (or specific test class)
   - Report test results

6. **Create Commit** (if requested)
   - If `--commit` flag: create git commit
   - Use descriptive commit message following project conventions
   - Include Co-Authored-By: Claude
   - Run git status to verify

7. **Summary**
   - List all files modified
   - Show diff summary (lines added/removed)
   - Confirm fix is complete

## Output Format

```markdown
🔧 Quick Fix: {description}

📋 Clarification:
   • Issue: {confirmed issue description}
   • Affected Files: {file count} file(s)
   • Run Tests: {yes/no}
   • Create Commit: {yes/no}

============================================================
🔍 Locating Affected Files
============================================================

Found {N} file(s) that need changes:
  • src/main/java/com/example/Service.java:42 - Issue: {specific issue}
  • src/main/java/com/example/Controller.java:18 - Issue: {specific issue}

✓ All files found (within 3-file limit)

============================================================
🛠️  Implementing Fix
============================================================

File: src/main/java/com/example/Service.java
  • Line 42: {description of change}

✅ Implementation complete

============================================================
✅ Verification
============================================================

Modified Files:
  ✓ src/main/java/com/example/Service.java (+3, -2)

{If --test flag:}
Running test verification...
✅ Tests passed (no errors)

{If --commit flag:}
Creating git commit...
✅ Commit created: abc1234 "[fix] {description}"

============================================================
📊 Summary
============================================================

Fix Complete: {description}

Changes:
  • Files Modified: {N}
  • Lines Added: {N}
  • Lines Removed: {N}

✅ Quick fix successfully applied!
```

## When NOT to Use This Command

Reject quick-fix if:

1. **Too Many Files**: Change requires >3 files
2. **Complex Changes**: Requires architectural decisions
3. **New Features**: Adding new functionality

## Examples

### Good Quick Fix Examples

✅ **Fix typo in error message**:
```bash
/quality:quick-fix "Fix typo 'occured' → 'occurred' in ProductService error messages"
```

✅ **Add missing null check**:
```bash
/quality:quick-fix "Add null check before accessing product.getSection() in ProductController"
```

✅ **Fix off-by-one error**:
```bash
/quality:quick-fix "Fix pagination offset calculation in ProductService.getAll()"
```

### Bad Quick Fix Examples

❌ **Too broad**: "Refactor authentication system"
❌ **New feature**: "Add password reset functionality"
❌ **Multiple concerns**: "Fix error handling and add logging and update validation"
❌ **Vague**: "Make it better"

## Notes

- For Java/Maven projects, use `mvn test -Dtest=ClassName` for specific test classes
- Quick fixes don't create documentation files
- Use git history for tracking
