**Goal:** make Jimothy drive to the pose `(24, 0)` all by itself.

## The driveToTarget loop

Open `MyBioBuzzLab.java` and find the `TODO` comment inside the
`runOpMode()` method. Replace it with this block:

```java
double targetX = 24.0;   // inches forward of start
double targetY = 0.0;    // inches left of start
double Kp = 0.04;        // "how hard do we push to close the error"

while (opModeIsActive()) {
    updatePose();
    double errorX = targetX - x;
    double errorY = targetY - y;
    double distance = Math.hypot(errorX, errorY);

    if (distance < 1.0) {
        setMecanum(0, 0, 0);          // stop; we're there
        telemetry.addLine("Arrived.");
        telemetry.update();
        break;
    }

    // Rotate world-frame error into robot-frame drive/strafe
    double cos = Math.cos(heading);
    double sin = Math.sin(heading);
    double forward = errorX * cos + errorY * sin;
    double strafe  = -errorX * sin + errorY * cos;

    setMecanum(forward * Kp, strafe * Kp, 0);

    telemetry.addData("Pose", "(%+.1f, %+.1f) @ %+.1f deg",
            x, y, Math.toDegrees(heading));
    telemetry.addData("Error", "%+.1f in", distance);
    telemetry.update();
}
```

Three things worth saying out loud:

1. **The controller is one line of math.** `output = Kp * error`.
   That's a proportional controller. If `Kp` is too small, Jimothy
   creeps; if too big, it overshoots and oscillates.
2. **Rotate error into robot frame.** The error `(errorX, errorY)` is in
   world coordinates (the field). Jimothy's wheels only know about its
   own body (forward / strafe). The two lines with `cos` and `sin` convert
   between them. This is the same trick the field-centric teleop uses.
3. **`setMecanum` already exists** at the bottom of `MyBioBuzzLab.java`.
   Three inputs: forward power, strafe power, turn power. You wrote one
   just like it in Lesson 11.

## Run it

Build and run. Pick MyBioBuzzLab, INIT, START.

What should you see?

- Jimothy drives 24 inches forward (toward the top of the field).
- Score HUD does not change (we're not scoring yet).
- Telemetry's `Error` shrinks toward 0; `Pose` ends near `(+24, 0, 0)`.

## Change one thing — predict first

Change `targetY = 0.0` to `targetY = 12.0`. Predict in your head: which
corner does Jimothy end up near? Then run and check.

Then change `Kp = 0.04` to `Kp = 0.15`. Predict what happens before you
run. (Hint: it won't be pretty.)

Press **Next** when you're done with Lesson 2.
