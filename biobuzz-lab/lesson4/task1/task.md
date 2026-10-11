**Goal:** write code that drives to a flower, runs the intake, and
confirms it worked by reading the score HUD.
**You'll learn:** closing the loop — write code, run it, use the scoreboard
as the ground truth.

## What "pickup" means in BIOBUZZ

In the real BIOBUZZ game, pollen balls sit in a **retrieval opening** at
the bottom of each flower. A robot drives up to the opening and spins
its intake to pull a ball in. In the sim, we skip modeling individual
balls and watch the behavior instead:

- If Jimothy's center is inside the yellow flower circle, **and**
- `intake_motor.getPower() > 0.3` (intake running forward),

then after about 300 ms the scoreboard increments `pollenCarried` by 1.
The HUD updates immediately. Your telemetry gets the new value next
cycle.

That rule means your code has two jobs:

1. Get Jimothy's center into one of the four flower circles.
2. Set intake power and keep it there until the HUD confirms pickup.

Press **Next** to extract a reusable `driveToTarget` method.
