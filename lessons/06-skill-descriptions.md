# Step 06 — The description decides everything

**Goal:** Prove by experiment that a skill's `description` is the **only** thing Claude uses to decide whether to use it.

## Why

At level 1 Claude sees only `name` + `description`. It hasn't read the body or the files, so a perfect body behind a vague description never gets used.

**A good description has three parts:**

| Part | Example |
|---|---|
| **What** it does, concretely | "Run AlertDesk's full pre-commit check (unit tests, then the CLI)…" |
| **When** to use it, in words users actually say | "Use before committing, or when asked to verify, check or validate…" |
| *(optional)* When **not** to use it | "Not for explaining code." |

## Do

Run each experiment in a **new session**, and restore the good description at the end.

| # | Set `description:` to | Then ask | Triggered? |
|---|---|---|---|
| A | `checks stuff` | *"I'm about to commit — verify everything."* | |
| B | `Use for any question about AlertDesk.` | *"Explain what the Deduper class does."* | |
| C | Your original good description | *"I'm about to commit — verify everything."* | |
| D | Your original good description | *"Explain what the Deduper class does."* | |

Expected pattern: A = too vague, so it's missed. B = too broad, so it fires on unrelated questions. C fires. D doesn't.

## Check yourself

**1. What are the three parts of a good skill description?**

<details><summary>Hint</summary>

What + when + …
</details>

<details><summary>Answer</summary>

What it does (concretely) + when to use it (trigger words users say) + optionally when not to use it.
</details>

**2. Claude never uses your skill automatically; it only runs when you type `/name`. What do you fix first?**

<details><summary>Hint</summary>

What's the only text Claude reads when deciding?
</details>

<details><summary>Answer</summary>

The `description`. Make it concrete and add the words people actually use when they need it.
</details>

**3. Why is a too-broad description bad, if the skill still "works"?**

<details><summary>Hint</summary>

What gets loaded every time it fires?
</details>

<details><summary>Answer</summary>

Every time it fires, the full body loads: wasted tokens, plus irrelevant instructions that can steer the answer the wrong way. That's the same context-rot problem a long CLAUDE.md has.
</details>
