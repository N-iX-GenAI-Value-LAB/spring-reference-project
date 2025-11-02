---
command: research
description: Analyzes codebase and creates comprehensive research documentation for a feature
creation-date: 2025-09-16 13:19 UTC+0300
last-update: 2025-09-16 13:19 UTC+0300
---

## Description

Analyzes existing code and researches changes needed to implement a specific feature or improvement. Creates comprehensive research documentation to guide implementation.

## Parameters

- `topic` (required): The research topic or feature to analyze. If not provided, the command will prompt for it.

## Validation Rules

1. Topic must be provided or user will be prompted
2. Topic will be converted to lowercase with hyphens for folder naming (e.g., "Migrate to Postgres" → "migrate-to-postgres")
3. Research folder must not conflict with existing folders (will create new or update existing)
4. All clarifying questions must be answered before proceeding

## Execution Steps

1. **Accept or Request Topic**
   - If topic provided: validate and sanitize for folder naming
   - If not provided: ask user to describe the research goal

2. **Clarification Phase** (MANDATORY)
   - Ask questions to ensure full comprehension of research goals
   - Clarify scope, constraints, and expected outcomes
   - **IMPORTANT**: If ANY aspect of the topic or research goals is unclear or ambiguous, STOP and ask the user for clarification
   - List all unclear points and ask specific questions
   - Confirm understanding with user before proceeding

3. **Deep Research and Analysis** (THINK HARD)
   - **Think hard** about the research topic and approach before analyzing
   - Analyze existing codebase relevant to the topic
   - **Consult FILE_DEPENDENCY_MAP.md** to understand existing file organization and dependencies
   - Search for related patterns, dependencies, and impacts
   - Research external resources if needed (documentation, best practices)
   - Identify potential challenges and solutions
   - Ensure thorough analysis and complete findings with all considerations documented

4. **Create Documentation Structure**
   - Create folder: `docs/{topic-hyphenated}/`
   - Generate research file: `research-{topic}.md`

4b. **Create Learnings Tracking File**
   - Create `learnings.json` in `docs/{topic}/` folder
   - Initialize: `{"featureName":"{topic}","created":"{ISO}","lastUpdated":"{ISO}","globalLearnings":[],"phases":[]}`
   - Use `date -u +"%Y-%m-%dT%H:%M:%SZ"` for timestamps

5. **Generate Research Document**
   - Add timestamp using system timezone
   - Fill in template sections with research findings
   - Ensure adherence to KISS and YAGNI principles

## Output Specification

### File Location
- Path: `docs/{topic-hyphenated}/research-{topic}.md`

### Document Template
```markdown
# Research: {Topic Title}

**Date**: {timestamp from date command}

## Executive Summary
[Brief overview of research findings and recommendations]

## Current State Analysis
[Analysis of existing codebase, architecture, and relevant components]

## Proposed Solution
[Detailed proposal following KISS and YAGNI principles]

## Dependencies
[List of dependencies, both internal and external]

## Implementation Considerations
[Key considerations, risks, and mitigation strategies]

### Learnings Tracking
Learnings tracked in `learnings.json` for Claude Code context priming. Consult before implementing tasks.
```

### Learnings Tracking File

**Location**: `docs/{topic-hyphenated}/learnings.json`

**Template**:
```json
{
  "featureName": "{topic-hyphenated}",
  "created": "{ISO timestamp}",
  "lastUpdated": "{ISO timestamp}",
  "globalLearnings": [],
  "phases": []
}
```

**Purpose**: Context continuity for Claude Code across tasks (internal use, not user-facing).

### Error Handling
- If topic is unclear: Request clarification with specific questions
- If folder exists: Continue and create/update research file
- If unable to analyze: Provide clear error message with suggestions

### Success Criteria
- Research document created with all sections completed
- Topic folder established for subsequent phases and tasks
- Clear, actionable findings that follow core principles