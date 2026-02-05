# Git Hooks for Spring Boot

## Installation

```bash
./scripts/install-hooks.sh
```

## Hooks

### pre-commit
- Compiles project (`mvn compile`)
- Validates architecture hierarchy

Bypass: `git commit --no-verify`

### pre-push
- Full build with tests (`mvn verify`)
- Architecture validation

Bypass: `git push --no-verify`

## Architecture Rules

```
Controller → Service → Repository
```

- **Controllers** can only inject Services
- **Services** can inject Repositories and other Services
- **Repositories** have no business dependencies
