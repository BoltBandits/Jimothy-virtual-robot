**Goal:** make Jimothy actually score into the hive.
**You'll learn:** two new ideas — **velocity-controlled motors** and
**waiting for a sensor** before doing the next thing.

## Why spin-up matters

The shooter is a flywheel. If you command "go!" at the exact moment you
feed a pollen ball, the wheel is still accelerating — it has no energy
to transfer. The ball dribbles out instead of being launched. Same thing
happens on the real robot and on paintball machines, cricket bowling
machines, and the pitching machines at a batting cage.

The fix is a spin-up:

1. Set the shooter to its target velocity.
2. **Wait** until `shooter.getVelocity()` says it has actually reached
   that velocity.
3. *Then* feed the ball with the windmill.

If you skip step 2, you waste pollen.

## Two new hardware things

`shooter_motor` is a **DcMotorEx**, not a plain DcMotor. The `Ex` adds
velocity control: `setVelocity(ticksPerSecond)` and `getVelocity()`.

The windmill is a **CRServo** — a continuous rotation servo. You already
met these in main-course Lesson 12. Same API as a motor:
`setPower(-1.0 to +1.0)`. On this robot, **negative power feeds inward**
toward the shooter (the real intake-to-shooter chain requires this
direction).

Press **Next** to add the hardware lookups.
