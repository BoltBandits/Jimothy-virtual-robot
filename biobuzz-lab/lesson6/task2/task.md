**Goal:** add a method that reports whether Jimothy is aimed at the hive.

## Add aimedAtHive()

Open `MyBioBuzzLab.java`. Near `turnToFace`, add this method:

```java
private static final double HIVE_X = 0.0;
private static final double HIVE_Y = 0.0;
private static final double HIVE_RADIUS_IN     = 18.0;              // generous: ~0.46 m
private static final double HIVE_HEADING_TOL   = Math.toRadians(5.0);

private boolean aimedAtHive() {
    double dx = HIVE_X - x;
    double dy = HIVE_Y - y;
    double distance = Math.hypot(dx, dy);
    double bearing  = Math.atan2(dy, dx);
    double hdgError = normalize(bearing - heading);

    return distance <= HIVE_RADIUS_IN
            && Math.abs(hdgError) <= HIVE_HEADING_TOL;
}
```

Three things worth saying out loud:

1. **The three constants at the top are a tiny config block.** They are
   the numbers you'd tune when the real robot behaves differently from
   the sim. Keep tunable numbers at the top.
2. **`distance` and `hdgError` are both signed-magnitude.** `distance`
   is always ≥ 0; `hdgError` is signed, so we `Math.abs` it before
   comparing to tolerance.
3. The method tells you NOTHING about the shooter or windmill — only
   geometry. The caller stacks the other checks on top. One method, one
   job.

## Try it in telemetry

Replace the main `while (opModeIsActive())` loop at the end of
`runOpMode()` with this diagnostic version:

```java
while (opModeIsActive()) {
    updatePose();
    double dx = HIVE_X - x;
    double dy = HIVE_Y - y;
    telemetry.addData("Pose", "(%+.1f, %+.1f) @ %+.1f deg",
            x, y, Math.toDegrees(heading));
    telemetry.addData("To hive", "%.1f in @ %+.1f deg bearing",
            Math.hypot(dx, dy),
            Math.toDegrees(Math.atan2(dy, dx)));
    telemetry.addData("Aimed at hive?", aimedAtHive() ? "YES" : "no");
    telemetry.update();
    idle();
}
```

Run it, then drive Jimothy manually. The on-screen gamepad still works
during a LinearOpMode — the sticks drive the robot through the Lesson 2
field-centric teleop... wait, no, this is YOUR OpMode now. The sticks do
nothing because you haven't wired them. Drive by **clicking and dragging
the robot** on the field.

Watch the telemetry change as you drag Jimothy near center and spin it.
The "YES" / "no" flips in real time.

Press **Next** for one more experiment.
