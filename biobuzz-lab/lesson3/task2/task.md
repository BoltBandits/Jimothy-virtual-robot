**Goal:** add a `turnToFace` method and point Jimothy at the north flower.

## Add a method

Open `MyBioBuzzLab.java` and add this method, next to `setMecanum`:

```java
private void turnToFace(double targetX, double targetY) {
    final double HEADING_KP = 1.5;
    final double TURN_TOLERANCE_RAD = Math.toRadians(2.0);
    final double MAX_TURN = 0.5;
    final double MIN_TURN = 0.10;

    while (opModeIsActive()) {
        updatePose();
        double targetBearing = Math.atan2(targetY - y, targetX - x);
        double error = normalize(targetBearing - heading);

        if (Math.abs(error) <= TURN_TOLERANCE_RAD) {
            setMecanum(0, 0, 0);
            return;
        }

        double turn = Range.clip(error * HEADING_KP, -MAX_TURN, MAX_TURN);
        if (Math.abs(turn) < MIN_TURN) turn = Math.copySign(MIN_TURN, turn);

        setMecanum(0, 0, turn);
        telemetry.addData("Facing", "err=%+.1f deg", Math.toDegrees(error));
        telemetry.update();
    }
}
```

A few things worth saying out loud:

1. `atan2(dy, dx)` — not `atan2(dx, dy)`. The order matters. In FTC
   convention, `x` is forward (world-frame) and `y` is left. The angle
   returned is measured from the +x axis, counter-clockwise. So "straight
   ahead" is 0, "90° left" is π/2.
2. `MIN_TURN` is a floor. Motors have **static friction** — at tiny
   turn commands they won't spin at all. By never asking for less than
   `0.10`, we guarantee the robot actually moves when we tell it to.
3. The method **blocks**. It doesn't return until Jimothy is pointed
   where you asked. Lesson 2's drive loop did the same thing.

## Call it

In `runOpMode()`, right after `resetPose()` (the second one), add:

```java
turnToFace(0, 60);   // face the north flower (y = +60 inches)
telemetry.addLine("Facing north flower");
telemetry.update();
sleep(2000);
```

The north flower is at the top of the field, so from the start
`(0, 0, 0°)` it is **90° to the left** (positive y). Predict: which way
will Jimothy spin, and roughly how far?

Run it. Verify your prediction.

## Change one thing

Change the target to `turnToFace(60, 0)` (east flower). Predict first.
Then change it to `turnToFace(-30, -30)` and predict again.

Press **Next** when you're done with Lesson 3.
