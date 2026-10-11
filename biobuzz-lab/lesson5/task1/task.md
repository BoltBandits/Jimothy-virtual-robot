**Goal:** score your first points by placing pollen in the garden.
**You'll learn:** `setPower` takes a **signed** number — the direction of
a motor is baked into the sign.

## Forward vs reverse

In Lesson 4 you ran `intake.setPower(0.6)` and the HUD counted pollen
**up**. Try this in your head: what should happen if you call
`intake.setPower(-0.6)` while inside a flower zone?

Answer: **nothing**. The game's pickup rule says
`intakePower > 0.3`, which is false when `intakePower = -0.6`.

But in the **garden** zones (the green circles in the two opposite
corners), the opposite rule applies. The game checks
`intakePower < -0.3`:

- Negative intake power = running the intake in reverse = **ejecting**.
- Positive intake power = running forward = **picking up**.

Same motor, same wire, same code. The sign of the number decides.

That's not a sim invention — it's how the physical Jimothy's intake is
wired. Reverse the intake motor in your TeleOp and you reverse the
rollers. The pollen comes back out.

## Where the gardens are

The two garden corners, in field inches from the center:

| Garden | (x, y) |
| --- | --- |
| Blue (top-left)      | `(-60, +60)` |
| Red  (bottom-right)  | `(+60, -60)` |

For this lesson we'll just pick one. Press **Next** to write the drop.
