#!/usr/bin/env bash
file_path=$(jq -r '.tool_input.file_path // empty')
case "$file_path" in
  *.java)
    cd "$CLAUDE_PROJECT_DIR" || exit 0
    if ! output=$(mvn -q test 2>&1); then
      echo "Tests failed after editing $file_path:" >&2
      echo "$output" | tail -n 40 >&2
      exit 2
    fi
    ;;
esac
exit 0
