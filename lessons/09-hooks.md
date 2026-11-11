# Step 09 — Hooks: rules that are enforced, not requested

**Goal:** Make two rules **deterministic**: tests run after every Java edit, and the sample data can't be edited.

## What a hook is

A hook is **your shell command** that Claude Code runs automatically at a lifecycle event. Hooks are defined **inside `settings.json`**, not in separate rule files.

| Event | When | Can block? |
|---|---|---|
| `PreToolUse` | Before a tool runs | **Yes** (exit 2 stops the tool) |
| `PostToolUse` | After a tool runs | No, it already ran; but exit 2 sends your message back to Claude |
| `UserPromptSubmit`, `Stop`, `SessionStart`, … | Other moments | Varies |

**Input:** JSON on stdin, e.g. `{"tool_name": "Edit", "tool_input": {"file_path": "..."}}`.
**Exit codes:** `0` = fine. `2` = block (Pre) or report back (Post); your **stderr** text is shown to Claude.

| CLAUDE.md says "run tests after edits" | Hook runs tests after edits |
|---|---|
| A request; the model may forget in a long session | Runs **every time**, by the program |
| Costs tokens | Costs no tokens until it reports something |

## Do

1. Create `.claude/hooks/protect-sample-data.sh`:
   ```bash
   #!/usr/bin/env bash
   file_path=$(jq -r '.tool_input.file_path // empty')
   case "$file_path" in
     */data/alerts.txt)
       echo "data/alerts.txt is protected sample data. Create a new file for test data instead." >&2
       exit 2
       ;;
   esac
   exit 0
   ```
2. Create `.claude/hooks/test-after-java-edit.sh`:
   ```bash
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
   ```
3. `chmod +x .claude/hooks/*.sh`
4. **Test the hooks without Claude.** This is the habit that saves hours:
   ```bash
   echo '{"tool_input":{"file_path":"/x/data/alerts.txt"}}' | .claude/hooks/protect-sample-data.sh; echo "exit=$?"
   echo '{"tool_input":{"file_path":"/x/README.md"}}' | .claude/hooks/protect-sample-data.sh; echo "exit=$?"
   ```
   Expect `exit=2` (with the message), then `exit=0`.
5. Add a `hooks` block to `.claude/settings.json`, next to `permissions`:
   ```json
   "hooks": {
     "PreToolUse": [
       {
         "matcher": "Edit|Write",
         "hooks": [
           { "type": "command", "command": "\"$CLAUDE_PROJECT_DIR\"/.claude/hooks/protect-sample-data.sh" }
         ]
       }
     ],
     "PostToolUse": [
       {
         "matcher": "Edit|Write",
         "hooks": [
           { "type": "command", "command": "\"$CLAUDE_PROJECT_DIR\"/.claude/hooks/test-after-java-edit.sh" }
         ]
       }
     ]
   }
   ```
   Check the JSON is valid: `jq . .claude/settings.json`
6. New session. Run `/hooks` and confirm both are listed.
7. Ask *"Add a new alert line to data/alerts.txt."* It should be blocked.
8. Ask *"In AlertParser, stop accepting lowercase severities."* That breaks a test. Watch the PostToolUse hook report the failure, and see what Claude does next. Undo with `git checkout src`.

## Observe

| Record | Value |
|---|---|
| Result of step 7 | |
| What Claude did after the failed-test report in step 8 | |

## Check yourself

**1. CLAUDE.md says "run tests after Java edits", and a hook does the same. What's the real difference?**

<details><summary>Hint</summary>

Who executes each one?
</details>

<details><summary>Answer</summary>

The CLAUDE.md line is a request the model may skip. The hook is executed by the program every time, so it's deterministic.
</details>

**2. Which event can *prevent* an action: PreToolUse or PostToolUse?**

<details><summary>Hint</summary>

Pre = before, Post = after.
</details>

<details><summary>Answer</summary>

PreToolUse, with exit code 2. PostToolUse runs after the tool already ran; it can only report back to Claude.
</details>

**3. Where are hooks defined?**

<details><summary>Hint</summary>

Not a `.md` file.
</details>

<details><summary>Answer</summary>

Inside `settings.json` (user, project or local). The scripts can live anywhere; `.claude/hooks/` is just a tidy convention.
</details>
