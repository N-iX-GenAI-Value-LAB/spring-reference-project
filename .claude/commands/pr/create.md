---
command: pr:create
description: Generate succinct PR summary and create pull request
---

## Description

Analyzes changes between the current branch and master, generates a succinct PR summary, and creates a pull request with that summary.

## Arguments

- `$ARGUMENTS` - Remote SSH key to use: `nix` or `workwave` (optional)

## SSH Key Configuration

If a remote argument is provided, use the corresponding SSH key:
- `nix` → `~/.ssh/nix`
- `workwave` → `~/.ssh/workwave`

If specified, set the environment variable for all git and gh operations:
```bash
GIT_SSH_COMMAND="ssh -i ~/.ssh/<remote> -o IdentitiesOnly=yes"
```

If no argument provided, use default git/gh authentication.

## Execution Steps

1. **Check Remote Argument (if provided)**
   - If `$ARGUMENTS` is provided but not `nix` or `workwave`, display error and exit
   - If `nix` or `workwave`, set `SSH_KEY_PATH` accordingly
   - If not provided, proceed without custom SSH key

2. **Analyze Branch Changes**
   - Run `git status` to verify current branch
   - Run `git log master..HEAD --oneline` to see commits
   - Run `git diff master...HEAD --stat` to see changed files
   - Run `git diff master...HEAD` to analyze actual changes

3. **Generate PR Summary**
   - Identify the main theme/purpose of changes
   - Group related changes together
   - Highlight security improvements, bug fixes, new features, or enhancements
   - List files changed with brief descriptions
   - Keep summary concise (3-5 bullet points for main changes)

4. **Verify Branch Status and Push**
   - Check if current branch tracks a remote
   - Verify if branch is up to date with remote
   - Push to remote with `-u` flag if needed:
     - With SSH key: `GIT_SSH_COMMAND="ssh -i <SSH_KEY_PATH> -o IdentitiesOnly=yes" git push -u origin <branch>`
     - Without SSH key: `git push -u origin <branch>`

5. **Create Pull Request**
   - Use `gh pr create`:
     - With SSH key: `GIT_SSH_COMMAND="ssh -i <SSH_KEY_PATH> -o IdentitiesOnly=yes" gh pr create --title "..." --body "..."`
     - Without SSH key: `gh pr create --title "..." --body "..."`
   - Format body with:
     - Brief overview paragraph
     - **Key Changes:** bullet points
     - **Files Changed:** list
     - **Testing:** if applicable
   - Set base branch to `master`
   - Use HEREDOC for proper formatting

6. **Output PR URL**
   - Display the created PR URL
   - Show PR number and title

## PR Body Format

```markdown
{Brief overview of what this PR does and why}

**Key Changes:**
- {Change 1}
- {Change 2}
- {Change 3}

**Files Changed:**
- `file1` - {description}
- `file2` - {description}

**Testing:**
- {How to test, if applicable}
```

## Example Usage

```bash
# Create PR using default authentication
/pr:create

# Create PR using workwave SSH key
/pr:create workwave

# Create PR using nix SSH key
/pr:create nix
```

## Example Output

```markdown
Remote: workwave (using ~/.ssh/workwave)
Branch: docker-build-improvements
Base: master
Commits: 6
Files changed: 2

✅ PR created: https://github.com/user/repo/pull/123

PR #123: Docker Build Security & Workflow Improvements
```

## Notes

- The remote argument (`nix` or `workwave`) is optional
- The command will fail if not in a git repository
- Requires `gh` CLI to be installed and authenticated
- Will prompt for manual input if PR title is ambiguous
- Always includes Claude Code footer in PR description
