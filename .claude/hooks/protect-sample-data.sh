#!/usr/bin/env bash
input=$(cat)
file_path=$(echo "$input" | jq -r '.tool_input.file_path // empty')
command=$(echo "$input" | jq -r '.tool_input.command // empty')
case "$file_path" in
  */data/alerts.txt)
    echo "data/alerts.txt is protected sample data. Create a new file for test data instead." >&2
    exit 2
    ;;
esac
case "$command" in
  *'>'*alerts.txt*|*tee*alerts.txt*|*sed\ -i*alerts.txt*)
    echo "data/alerts.txt is protected sample data. Create a new file for test data instead." >&2
    exit 2
    ;;
esac
exit 0