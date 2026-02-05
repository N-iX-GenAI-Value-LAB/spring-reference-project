# Claude Code SDLC Workflow

Quick reference for using Claude Code commands in a Spring Boot project.

## Workflow Phases

```
Understand → Develop → Review → Integrate → Maintain
                                               ↓
              ← ← ← ← ← ← ← ← ← ← ← ← ← ← ← ← ←
```

### 1. Understand

Explore the codebase and catch up on recent changes.

| Command | Purpose |
|---------|---------|
| `/explain` | Search and explain code, architecture, or patterns |
| `/git:catchup` | Summarize recent commits and identify what changed |

**When:** Starting work, returning after time away, onboarding new team members.

### 2. Develop

Implement changes following KISS and YAGNI principles.

| Command | Purpose |
|---------|---------|
| `/quality:quick-fix` | Small fixes (≤3 files) without heavy workflow |
| `/git:commit` | Create commit with contextual prefix (`[dev]`, `[fix]`, etc.) |

**When:** Bug fixes, feature implementation, any code changes.

### 3. Review

Run quality gates and self-review before creating a PR.

| Command | Purpose |
|---------|---------|
| `/pr:checks` | Run compile + tests (`mvn clean compile && mvn test`) |
| `/pr:review:local` | Dispatch agent to review branch changes |
| `/quality:simplify` | Check for unnecessary complexity or dead code |

**When:** After implementation, before creating PR.

### 4. Integrate

Create pull request and merge when approved.

| Command | Purpose |
|---------|---------|
| `/pr:create` | Generate PR summary and create pull request |
| `/pr:merge` | Squash-merge PR, combining commit messages |

**When:** Code is ready for the main branch.

### 5. Maintain

Keep branches healthy and manage technical debt.

| Command | Purpose |
|---------|---------|
| `/git:sync` | Rebase current branch on main/master |
| `/git:cleanup` | Delete merged local branches |
| `/quality:find-large-files` | Identify files that need splitting |

**When:** Regular housekeeping, before starting new work.

## Quick Paths

| Scenario | Commands |
|----------|----------|
| **Small fix** | `/quality:quick-fix` → `/git:commit` → `/pr:create` |
| **Feature work** | Follow all 5 phases |
| **Returning to project** | `/git:catchup` → `/git:sync` |

## Build Commands

```bash
mvn clean package      # Build
mvn test               # Run all tests
mvn spring-boot:run    # Start application
```

## Visual Workflow

See the interactive diagram: [workflow.html](../src/main/resources/static/workflow.html)
