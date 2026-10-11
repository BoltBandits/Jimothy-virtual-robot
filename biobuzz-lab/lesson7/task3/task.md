**Goal:** full chain: pickup → drive → aim → launch. Score at least one
pollen into the hive.

## Put it together

In `runOpMode()`, replace the main body (everything after the second
`resetPose()`) with this sequence. **Keep** your pickup block from
Lesson 4 at the top:

```java
// 1. Pickup from north flower (Lesson 4 code — reuse unchanged)
driveToTarget(0, 60);
turnToFace(0, 72);
intake.setPower(0.6);
while (opModeIsActive() && BioBuzzScoreboard.getPollenCarried() < 2) {
    telemetry.addData("Collecting",
            BioBuzzScoreboard.getPollenCarried() + " / 2");
    telemetry.update();
    idle();
}
intake.setPower(0);

// 2. Go to the hive and aim (Lesson 6)
driveToTarget(HIVE_X, HIVE_Y);
turnToFace(HIVE_X - 1, HIVE_Y);
while (opModeIsActive() && !aimedAtHive()) {
    telemetry.addLine("Settling aim...");
    telemetry.update();
    idle();
}

// 3. Launch one ball into the hive (this lesson)
telemetry.addLine("Aim locked. Launching.");
telemetry.update();
launchOne(shooter, windmill);

telemetry.addLine("One in the hive.");
telemetry.addData("Final score", BioBuzzScoreboard.getAllianceScore());
telemetry.update();
sleep(3000);
```

## What you should see

1. Jimothy picks up 2 pollen at the north flower (HUD: `Carried: 2`).
2. Jimothy drives south to the hive at `(0, 0)`.
3. Jimothy pivots to point at the hive (small turn, since we drove south).
4. Telemetry shows the shooter spinning up to ~300 ticks/s.
5. "Aim locked. Launching." appears.
6. HUD: `Carried` goes 2 → 1, `Hive` goes 0 → 1, `Score` goes +2.
7. Jimothy stops with 1 pollen still carried.

If `Hive` never increments, check:

- Shooter velocity actually reached the target? (Spin-up telemetry.)
- Is `aimedAtHive()` returning YES? (Add telemetry inside the aim loop.)
- Is the windmill power **negative**? Positive feeds OUT (unload), not
  into the shooter.

## What you just did

You wrote an **autonomous routine** — not a teleop. There are no
gamepads in your code. Jimothy drives itself from start to a scored
hive point. On the physical robot, that is 30 seconds of a real match's
autonomous period doing meaningful work.

Everything from Lessons 2–7 was the foundation for this moment:

| Idea | Where it came from |
| --- | --- |
| Pose | Lesson 2 |
| driveToTarget | Lesson 2 |
| turnToFace | Lesson 3 |
| Watch the scoreboard, not the clock | Lesson 4 |
| Sign is direction | Lesson 5 |
| Aim as a boolean | Lesson 6 |
| Spin up then feed | this lesson |

Lesson 8 will take this routine and turn it into a real match plan with
multiple pickups and the garden drop at the end.

## Change one thing — predict first

- Change `SHOOTER_TARGET_VELOCITY = 300` to `SHOOTER_TARGET_VELOCITY =
  50`. Will `launchOne` return? What does the HUD show?
- Change the pickup target to 3 pollen instead of 2, and add a second
  `launchOne(shooter, windmill)` call. What score do you expect?

That's slice 2 of BioBuzz Lab.
