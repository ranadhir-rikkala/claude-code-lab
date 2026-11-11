#!/usr/bin/env bash
file_path=$(jq -r '.tool_input.file_path // empty')
case "$file_path" in
  */data/alerts.txt)
    echo "data/alerts.txt is protected sample data. Create a new file for test data instead." >&2
    exit 2
    ;;
esac
exit 0
