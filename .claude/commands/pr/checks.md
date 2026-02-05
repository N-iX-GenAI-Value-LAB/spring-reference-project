# Run Pre-PR Checks

Run local checks (compile, tests) before creating a pull request.

## Execution Steps

1. **Detect Project Type**
   Check for project files in order:
   - `pom.xml` → Maven (Java)
   - `build.gradle` or `build.gradle.kts` → Gradle (Java)
   - `package.json` → Node.js/TypeScript
   - `Cargo.toml` → Rust
   - `go.mod` → Go
   - `pyproject.toml` or `requirements.txt` → Python

2. **Identify Available Tasks**
   For Maven, check `pom.xml` for plugins and profiles:
   - Compile: `mvn compile`
   - Test: `mvn test`
   - Package: `mvn package`
   - Checkstyle/SpotBugs if configured

3. **Run Checks in Order**

   **Maven (Java):**
   ```bash
   mvn clean compile         # Compile sources
   mvn test                  # Run unit tests
   ```

   **Gradle (Java):**
   ```bash
   ./gradlew clean build     # Compile and test
   ./gradlew check           # Run all checks
   ```

   **Node.js/TypeScript:**
   ```bash
   npm run lint          # or pnpm/yarn
   npm run type-check    # if available
   npm run test          # if available
   ```

   **Rust:**
   ```bash
   cargo fmt --check
   cargo clippy -- -D warnings
   cargo test
   ```

   **Go:**
   ```bash
   go fmt ./...
   go vet ./...
   go test ./...
   ```

   **Python:**
   ```bash
   ruff check .          # or flake8
   mypy .                # if configured
   pytest                # if tests exist
   ```

4. **Report Results**
   - Show pass/fail for each check
   - On failure: display error output, suggest fixes
   - On success: confirm ready for PR

## Output Format

### Success
```markdown
Project: reference.spring.project (Maven + Java)
Build tool: Maven

Checks:
  ✓ Compile .................... passed
  ✓ Tests (TestNG) ............. 12 passed, 0 failed

✓ All checks passed - ready for /pr:create
```

### Failure
```markdown
Project: reference.spring.project (Maven + Java)

Checks:
  ✓ Compile .................... passed
  ✗ Tests (TestNG) ............. 2 errors

Errors:
  ProductControllerTest.testGetProduct - Expected 200 but got 404
  SectionServiceTest.testCreate - NullPointerException at line 42

Fix test failures and run /pr:checks again.
```

## Notes

- Detects build tool from project files
- Skips checks that don't exist in the project
- Runs checks sequentially, stops on first failure (fail-fast)
- Use `--continue` to run all checks even if some fail
