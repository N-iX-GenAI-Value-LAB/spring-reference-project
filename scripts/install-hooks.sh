#!/bin/bash

# Install git hooks from scripts/git-hooks to .git/hooks

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
HOOKS_SRC="$SCRIPT_DIR/git-hooks"
HOOKS_DST="$(git rev-parse --git-dir)/hooks"

echo "Installing git hooks..."

for hook in "$HOOKS_SRC"/*; do
    [[ ! -f "$hook" ]] && continue
    name=$(basename "$hook")
    [[ "$name" == "README.md" ]] && continue

    cp "$hook" "$HOOKS_DST/$name"
    chmod +x "$HOOKS_DST/$name"
    echo "  ✓ Installed $name"
done

echo ""
echo "Done! Hooks installed to $HOOKS_DST"
