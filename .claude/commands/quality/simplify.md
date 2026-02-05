---
command: quality:simplify
description: Review recent changes for unnecessary complexity, dead code, and bloated abstractions
---

## Description

Reviews code for unnecessary complexity and suggests simplifications. Addresses the "1000 lines when 100 would do" problem by systematically identifying:
- Unused code and dead exports
- Over-abstracted patterns
- Unnecessary error handling
- Premature generalizations
- Code that could be inlined

## Parameters

- `path` (optional): Specific file or directory to review. Defaults to recent git changes.
- `--staged` (optional): Review only staged changes
- `--aggressive` (optional): Suggest more radical simplifications

## Execution Steps

### 1. Identify Scope

If `path` provided:
- Review that specific file/directory

If no path:
- Get list of recently modified files from git (last commit or staged changes)
- Focus on files with significant additions

```bash
git diff --name-only HEAD~1  # or --staged if flag set
```

### 2. Analyze Each File

For each file, check:

**Dead Code Detection:**
- Public methods not called anywhere else
- Private methods never called within the class
- Fields assigned but never read
- Commented-out code blocks
- Unused imports

**Over-Abstraction Detection:**
- Helper methods used only once (should inline)
- Abstract classes with single implementation
- Configuration objects for things that never change
- Generic utilities solving non-generic problems
- Interfaces with single implementor

**Unnecessary Complexity:**
- Try/catch blocks around code that can't throw checked exceptions
- Null checks for values annotated @NonNull or from trusted sources
- Feature flags that are always on/off
- Backwards compatibility code for deprecated paths
- Error handling for impossible states

**Bloat Patterns:**
- Large switch statements that could be lookup maps
- Repeated similar code that's NOT worth abstracting (3 lines is fine)
- Overly defensive programming internally
- Excessive logging
- Javadoc for self-evident methods

### 3. Generate Report

```markdown
## Simplification Report: {scope}

### Summary
- Files analyzed: {N}
- Potential simplifications: {N}
- Estimated lines removable: {N}

---

### {filename}

**Dead Code** (can delete):
- Line 42: `unusedHelper()` - method never called
- Line 89-95: Commented-out code block
- Line 120: `import java.util.LinkedList` - never used

**Over-Abstraction** (should inline):
- Line 30: `formatDate()` - used once at line 156, inline it
- Line 200: `ConfigManager` class - just use constants or @Value

**Unnecessary Complexity** (can simplify):
- Line 78: try/catch around method that only throws unchecked exceptions
- Line 145: null check on @NonNull parameter

**Suggested Refactor:**
```diff
- String result = this.configManager.get("timeout");
+ private static final int TIMEOUT = 5000;
```

---

### Quick Wins (immediate action)

1. Delete `src/main/java/com/example/DeprecatedService.java` - not used anywhere
2. Inline `formatUserId()` in `AuthService.java:45`
3. Remove unused import `java.util.LinkedList` from `ProductService.java`

### Discussion Items (need confirmation)

1. `AbstractBaseRepository` has only `UserRepository` - collapse?
2. `@Transactional` on read-only methods - necessary?
```

### 4. Interactive Mode

After report, ask:
```
Would you like me to:
1. Apply quick wins automatically
2. Walk through each suggestion
3. Focus on a specific file
4. Exit (review manually)
```

## Success Criteria

- All files in scope analyzed
- Dead code identified with evidence
- Over-abstractions flagged with inline suggestions
- Actionable report with line numbers
- No false positives (conservative by default)

## Java-Specific Patterns to Flag

### Spring Boot Anti-Patterns
- `@Autowired` on fields (prefer constructor injection)
- `@Transactional` on private methods (doesn't work)
- Unnecessary `@Component` on classes only used as beans elsewhere
- Empty `@Configuration` classes

### Common Java Bloat
- StringBuilder for simple concatenations
- Explicit `toString()` calls in string concatenation
- `if (x == true)` instead of `if (x)`
- `Collections.emptyList()` when `List.of()` works
- Verbose Optional chains that could be simpler

### Exception Handling
- Catching `Exception` instead of specific types
- Empty catch blocks
- Logging and rethrowing the same exception
- Try-with-resources where not needed

## When NOT to Use

- Before understanding the codebase (use exploration first)
- On third-party/vendor code
- On generated files (entities from Hibernate tools, etc.)

## Example Usage

```bash
# Review recent changes
/quality:simplify

# Review specific file
/quality:simplify src/main/java/com/nix/service/ProductService.java

# Review staged changes before commit
/quality:simplify --staged

# Aggressive mode (more suggestions, some may be controversial)
/quality:simplify --aggressive
```

## Philosophy

This command embodies the principle: **"The best code is no code."**

Every line of code is a liability:
- It must be read and understood
- It must be maintained
- It can contain bugs
- It adds to compile time

Simplification isn't about making code "clever" - it's about making it **minimal** while preserving clarity and correctness.
