**Goal:** run a stock OpMode on Jimothy's twin and watch the score HUD.

## Try it

In the sim, in the **Op Mode** dropdown, pick **Jimothee Field-Centric
TeleOp**. Press **INIT**, then **START**.

Now drive Jimothy around with the on-screen gamepad (the sim pops up a
virtual gamepad; left stick translates, right stick turns).

Try this specifically:

1. Drive Jimothy into the **yellow circle** at the top (the north
   flower). Nothing happens — the scoreboard still shows `Carried: 0`.
2. On the virtual gamepad, press and hold **gamepad 2 → right trigger**
   (that is the intake trigger in this teleop).
3. The HUD should count up: `Carried: 1`, `Carried: 2`, …

That is a scoring action. You just picked up pollen because:

- Jimothy's position was inside the flower zone, AND
- Jimothy's `intake_motor` was running (right trigger sets its power).

Now drive to the orange circle (the hive) in the center.

- Hold **gamepad 2 → right bumper** (spins the shooter and feeds the
  windmill).
- The HUD should show `Hive: 1`, `Hive: 2`, …, and your `Score` ticks up.

You did not write any of that code. The teleop you ran was goBILDA's
`BioBuzzStarterbotTeleopMecanum` — the one the pros wrote, that you'll
read line by line in Lesson 18 of the main course.

## Why this matters

The whole BIOBUZZ game — intake, launch, score — just worked, in the
simulator, because the hardware names and controls match. Your job in
the next lessons is to write **your own** scoring code, piece by piece,
so you understand every line of it.

Close the sim window to end the OpMode. Then press **Next** to move on to
Lesson 2.
