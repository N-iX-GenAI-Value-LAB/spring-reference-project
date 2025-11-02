#!/bin/bash
# Read JSON input from stdin
input=$(cat)

# Helper functions for common extractions
get_model_name() { echo "$input" | jq -r '.model.display_name'; }
get_current_dir() { echo "$input" | jq -r '.workspace.current_dir'; }
get_project_dir() { echo "$input" | jq -r '.workspace.project_dir'; }
get_version() { echo "$input" | jq -r '.version'; }
get_cost() { echo "$input" | jq -r '.cost.total_cost_usd'; }
get_duration() { echo "$input" | jq -r '.cost.total_duration_ms'; }
get_lines_added() { echo "$input" | jq -r '.cost.total_lines_added'; }
get_lines_removed() { echo "$input" | jq -r '.cost.total_lines_removed'; }

# Use the helpers
MODEL_DISPLAY="🤖 $(get_model_name)"
CURRENT_DIR=$(get_current_dir)
# Extract values using jq
TRANSCRIPT_PATH=$(echo "$input" | jq -r '.transcript_path')

# Show git branch if in a git repo
GIT_BRANCH=""
if git rev-parse --git-dir > /dev/null 2>&1; then
    BRANCH=$(git branch --show-current 2>/dev/null)
    if [ -n "$BRANCH" ]; then
        GIT_BRANCH=" | 🌿 $BRANCH"
    fi
fi

# Function to estimate tokens from text (rough approximation: ~4 characters per token)
estimate_tokens() {
    local text="$1"
    local char_count=$(echo "$text" | wc -c)
    echo $((char_count / 4))
}

# Calculate token usage from transcript
USED_TOKENS="?"
ESTIMATED_TOKENS=0
if [ -n "$TRANSCRIPT_PATH" ] && [ -f "$TRANSCRIPT_PATH" ]; then
    # Read the transcript and estimate tokens
    if [ -r "$TRANSCRIPT_PATH" ]; then
        TRANSCRIPT_CONTENT=$(cat "$TRANSCRIPT_PATH" 2>/dev/null)
        if [ -n "$TRANSCRIPT_CONTENT" ]; then
            ESTIMATED_TOKENS=$(estimate_tokens "$TRANSCRIPT_CONTENT")
            # Format with k suffix for readability
            if [ "$ESTIMATED_TOKENS" -gt 1000 ]; then
                USED_TOKENS="$((ESTIMATED_TOKENS / 1000))K"
            else
                USED_TOKENS="$ESTIMATED_TOKENS"
            fi
        fi
    fi
fi

# Set context limits based on model (in actual token numbers for calculation)
CONTEXT_LIMIT_TOKENS=200000
case "$MODEL_DISPLAY" in
    *"Claude Sonnet 4"*|*"Sonnet 4"*)
        CONTEXT_LIMIT_TOKENS=200000
        ;;
    *"Opus 4.1"*)
        CONTEXT_LIMIT_TOKENS=200000
        ;;
    *)
        CONTEXT_LIMIT_TOKENS=0
        ;;
esac

# Calculate percentage of context used
PERCENTAGE="?"
if [ "$CONTEXT_LIMIT_TOKENS" -gt 0 ] && [ "$ESTIMATED_TOKENS" -gt 0 ]; then
    # Calculate percentage and round up using ceiling function
    PERCENTAGE=$(echo "scale=0; ($ESTIMATED_TOKENS * 100 + $CONTEXT_LIMIT_TOKENS - 1) / $CONTEXT_LIMIT_TOKENS" | bc)
    # Ensure it doesn't exceed 100%
    if [ "$PERCENTAGE" -gt 100 ]; then
        PERCENTAGE=100
    fi
fi

TOKEN_DISPLAY=" | 📊 ${PERCENTAGE}%"
TOTAL_COST=$(get_cost)

echo "[$MODEL_DISPLAY] 📁 ${CURRENT_DIR##*/}$GIT_BRANCH$TOKEN_DISPLAY | $TOTAL_COST$"
