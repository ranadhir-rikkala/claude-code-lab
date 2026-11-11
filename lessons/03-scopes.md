# Step 03 — Scopes: home, project, subfolder, memory

**Goal:** Put each rule at the right scope, and see a subfolder `CLAUDE.md` load **lazily**.

## The four scopes

| Scope | File | Loads | Shared? | Written by |
|---|---|---|---|---|
| You, every project | `~/.claude/CLAUDE.md` | Every session, everywhere | No | You |
| This project | `./CLAUDE.md`, `./AGENTS.md` | Every session in this repo | Yes, via git | You / team |
| One subfolder | `data/CLAUDE.md` | Only when Claude reads or edits files in `data/` | Yes, via git | You / team |
| Auto memory | `~/.claude/projects/<folder>/memory/` | Index at start, for that folder only | No | **Claude**, from what you tell it |

Subfolder files are the "load only when relevant" idea applied to rules: data-format rules cost nothing while Claude works on Java code.

## Do

**A. Home scope**
1. You don't have `~/.claude/CLAUDE.md` yet. Create it with rules you want in **every** repo, for example:
   ```markdown
   # My rules (all projects)

   - Never run git push. Commit locally and give me the push command.
   - Never add AI co-author or "generated with" lines to commits or PRs.
   ```
   Don't add language rules like "always Java": some of your repos are Python.
2. Start `claude` in a **different** folder (e.g. `~`) and run `/memory`. Is your home CLAUDE.md listed?

**B. Subfolder scope**
1. Create `data/CLAUDE.md`:
   ```markdown
   # data/

   - Format: `service|SEVERITY|message`. SEVERITY is INFO, WARN or CRITICAL.
   - Lines starting with `#` are comments.
   - Never edit `alerts.txt`. Create a new file for test data.
   ```
2. New session in the lab. Ask: *"Which CLAUDE.md files are in your context right now?"* Is `data/CLAUDE.md` there?
3. Ask: *"Read data/alerts.txt and tell me how many CRITICAL alerts there are."*
4. Ask again: *"Which CLAUDE.md files are in your context now?"*

## Observe

| Record | Value |
|---|---|
| Home CLAUDE.md listed in another folder? | |
| `data/CLAUDE.md` loaded at start? | |
| `data/CLAUDE.md` loaded after reading `data/`? | |

## Check yourself

**1. "Never push" should apply in every repo you own. Which file, and where?**

<details><summary>Hint</summary>

Every repo means home or project scope? Write the full path.
</details>

<details><summary>Answer</summary>

`~/.claude/CLAUDE.md`, the user-level file in your home folder. A repo's CLAUDE.md only applies to that repo.
</details>

**2. Why doesn't `data/CLAUDE.md` load at session start? What's the benefit?**

<details><summary>Hint</summary>

Same idea as skills: only when relevant.
</details>

<details><summary>Answer</summary>

Subfolder CLAUDE.md files load when Claude works with files in that folder. Rules for one area don't cost tokens or attention while Claude works elsewhere.
</details>

**3. What's the difference between `~/.claude/CLAUDE.md` and auto memory?**

<details><summary>Hint</summary>

Who writes each one? Which one would you trust for a hard rule?
</details>

<details><summary>Answer</summary>

You write CLAUDE.md, so it's a guaranteed, deliberate rule. Claude writes memory from your conversations, so it's learned preferences and facts, kept per project folder. Hard rules belong in CLAUDE.md.
</details>
