#!/bin/bash

# Architecture Hierarchy Checker for Spring Boot
# Enforces: Controllers → Services → Repositories
#
# Rules:
#   - Controllers can only inject Services (not Repositories)
#   - Services can inject Repositories and other Services
#   - Repositories should not inject Services or Controllers
#
# Usage:
#   ./scripts/validate/architecture.sh [--staged]

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'

VIOLATIONS=0
STAGED_ONLY=false

[[ "$1" == "--staged" ]] && STAGED_ONLY=true

SRC_DIR="src/main/java"

get_files() {
    local pattern="$1"
    if $STAGED_ONLY; then
        git diff --cached --name-only --diff-filter=ACM | grep -E "$pattern" || true
    else
        find "$SRC_DIR" -type f -name "*.java" 2>/dev/null | grep -E "$pattern" || true
    fi
}

check_imports() {
    local file="$1"
    local layer="$2"
    shift 2
    local forbidden=("$@")

    for pattern in "${forbidden[@]}"; do
        local matches
        matches=$(grep -n "import.*$pattern" "$file" 2>/dev/null || true)
        if [[ -n "$matches" ]]; then
            while IFS= read -r match; do
                local line_num=$(echo "$match" | cut -d: -f1)
                echo -e "${RED}✗${NC} $file:$line_num"
                echo "    $layer cannot import $pattern"
                VIOLATIONS=1
            done <<< "$matches"
        fi
    done
}

check_injections() {
    local file="$1"
    local layer="$2"
    shift 2
    local forbidden=("$@")

    for pattern in "${forbidden[@]}"; do
        # Check @Autowired fields and constructor params
        local matches
        matches=$(grep -nE "(private|@Autowired).*$pattern" "$file" 2>/dev/null || true)
        if [[ -n "$matches" ]]; then
            while IFS= read -r match; do
                local line_num=$(echo "$match" | cut -d: -f1)
                echo -e "${RED}✗${NC} $file:$line_num"
                echo "    $layer cannot inject $pattern"
                VIOLATIONS=1
            done <<< "$matches"
        fi
    done
}

echo "Checking Spring Boot architecture hierarchy..."
echo ""

# Check Controllers - cannot inject Repositories directly
CONTROLLERS=$(get_files 'Controller\.java$')
if [[ -n "$CONTROLLERS" ]]; then
    while IFS= read -r file; do
        [[ -z "$file" ]] && continue
        check_imports "$file" "Controller" "repository" "Repository"
        check_injections "$file" "Controller" "Repository"
    done <<< "$CONTROLLERS"
fi

# Check Repositories - should not inject Services or Controllers
REPOSITORIES=$(get_files 'Repository\.java$|RepositoryImpl\.java$')
if [[ -n "$REPOSITORIES" ]]; then
    while IFS= read -r file; do
        [[ -z "$file" ]] && continue
        check_imports "$file" "Repository" "service" "Service" "controller" "Controller"
        check_injections "$file" "Repository" "Service" "Controller"
    done <<< "$REPOSITORIES"
fi

echo ""

if [[ $VIOLATIONS -eq 1 ]]; then
    echo "----------------------------------------"
    echo -e "${RED}✗ Architecture violations found!${NC}"
    echo ""
    echo "Required hierarchy: Controller → Service → Repository"
    echo ""
    echo "  - Controllers inject Services only"
    echo "  - Services inject Repositories and other Services"
    echo "  - Repositories have no business dependencies"
    echo "----------------------------------------"
    exit 1
else
    echo -e "${GREEN}✓${NC} Architecture check passed"
    exit 0
fi
