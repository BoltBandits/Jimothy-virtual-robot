**Goal:** turn Lesson 2's drive loop into a reusable method, and add an
intake handle.

## Refactor driveTo

Open `MyBioBuzzLab.java`. In Lesson 2 you wrote a drive loop with
hard-coded `targetX = 24, targetY = 0`. Lift the whole loop into a
method, next to `turnToFace`:

```java
private void driveToTarget(double targetX, double targetY) {
    final double Kp = 0.04;
    while (opModeIsActive()) {
        updatePose();
        double errorX = targetX - x;
        double errorY = targetY - y;
        double distance = Math.hypot(errorX, errorY);
        if (distance < 1.0) {
            setMecanum(0, 0, 0);
            return;
        }
        double cos = Math.cos(heading);
        double sin = Math.sin(heading);
        double forward = errorX * cos + errorY * sin;
        double strafe  = -errorX * sin + errorY * cos;
        setMecanum(forward * Kp, strafe * Kp, 0);
        telemetry.addData("Target", "(%+.1f, %+.1f)", targetX, targetY);
        telemetry.addData("Pose", "(%+.1f, %+.1f) @ %+.1f deg",
                x, y, Math.toDegrees(heading));
        telemetry.update();
    }
}
```

That's the same code as before — now with inputs for `targetX` and
`targetY`. You can call `driveToTarget(0, 60)` to go north,
`driveToTarget(60, 0)` to go east, and so on.

## Grab the intake motor

At the top of `runOpMode()`, next to the drive motor lookups, add:

```java
DcMotor intake = hardwareMap.get(DcMotor.class, "intake_motor");
```

Three things to notice:

1. We're not changing the hardware name. `intake_motor` matches
   `starter_bot.xml`, so this exact line also compiles on physical
   Jimothy.
2. We don't set a direction. For now, "positive = forward intake" is
   fine for both the sim and the real motor.
3. We don't touch `setMode`. The intake doesn't care about position
   control for pickup.

Press **Next** to write the pickup routine.
