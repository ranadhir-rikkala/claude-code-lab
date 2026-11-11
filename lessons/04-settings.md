# Step 04 — Settings and permissions

**Goal:** Control what Claude may do without asking. This is **config, not context**: settings are read by the program and use no tokens.

## The settings files

| File | Scope | Commit? |
|---|---|---|
| `~/.claude/settings.json` | You, all projects | No (it's in your home folder) |
| `.claude/settings.json` | Team, this repo | **Yes** |
| `.claude/settings.local.json` | You, this repo | **No** (already in `.gitignore`) |

**Permission rules:**

| Rule | Meaning |
|---|---|
| `"allow": ["Bash(mvn -q test:*)"]` | Run commands starting with `mvn -q test` without asking |
| `"deny": ["Read(./secrets/**)"]` | Never read anything under `secrets/` |
| `ask` | Always ask, even if a broader allow rule matches |

**Deny beats allow.** The rules from all files are merged.

**Prompt vs permission:** "Don't read secrets" in CLAUDE.md is a *request* the model can forget. A deny rule is *enforced* by the program.

## Do

1. Create `.claude/settings.json` (team rules):
   ```json
   {
     "permissions": {
       "allow": [
         "Bash(mvn -q test:*)",
         "Bash(./scripts/run.sh:*)"
       ],
       "deny": [
         "Read(./secrets/**)"
       ]
     }
   }
   ```
2. Create `.claude/settings.local.json` (just you):
   ```json
   {
     "permissions": {
       "allow": [
         "Bash(git status:*)",
         "Bash(git diff:*)"
       ]
     }
   }
   ```
3. Run `git status`. Which of the two files shows up? Why?
4. New session. Run `/permissions` to see the merged rules.
5. Ask *"Run the tests."* Was there a permission prompt?
6. Ask *"Read secrets/fake-token.txt."* What happens?
7. Run `/context`. Do the settings appear anywhere in the token count?

## Observe

| Record | Value |
|---|---|
| File shown by `git status` | |
| Prompt when running tests? | |
| Result of reading the secret | |
| Settings visible in `/context`? | |

## Check yourself

**1. Why don't settings files show up in `/context`?**

<details><summary>Hint</summary>

Who reads settings.json: the model or the program?
</details>

<details><summary>Answer</summary>

The Claude Code **program** reads them as config and enforces them. They're never sent to the model as text, so they cost no tokens.
</details>

**2. The team should run tests without prompts. Only you want `git diff` without prompts. Which files?**

<details><summary>Hint</summary>

Team = committed. Just you = not committed.
</details>

<details><summary>Answer</summary>

Tests → `.claude/settings.json` (committed). `git diff` → `.claude/settings.local.json` (gitignored).
</details>

**3. `allow` has `Read(**)` and `deny` has `Read(./secrets/**)`. Can Claude read the secret?**

<details><summary>Hint</summary>

Which rule type wins on conflict?
</details>

<details><summary>Answer</summary>

No. Deny always beats allow.
</details>
