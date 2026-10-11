**Goal:** pick up 2 pollen from the north flower, drive to the blue
garden, and eject them. Watch the score tick up.

## Add the drop

Open `MyBioBuzzLab.java`. **Keep the Lesson 4 pickup code** — don't
delete it. Just add the garden run right after. After the
`sleep(2000)` at the end of your pickup block, add:

```java
// Head to the blue garden corner
driveToTarget(-60, 60);
telemetry.addLine("At blue garden; ejecting");
telemetry.update();

// Reverse the intake until we're empty
intake.setPower(-0.6);
while (opModeIsActive() && BioBuzzScoreboard.getPollenCarried() > 0) {
    telemetry.addData("Carrying",
            BioBuzzScoreboard.getPollenCarried() + " pollen");
    telemetry.addData("Garden total",
            BioBuzzScoreboard.getPollenInGarden());
    telemetry.update();
    idle();
}
intake.setPower(0);
telemetry.addLine("Garden empty of robot pollen.");
telemetry.addData("Final score", BioBuzzScoreboard.getAllianceScore());
telemetry.update();
sleep(3000);
```

## What you should see

1. Jimothy drives to the north flower and picks up 2 pollen (Lesson 4
   behavior, unchanged).
2. Jimothy drives from `(0, +60)` to `(-60, +60)` — straight west along
   the top of the field.
3. The HUD's `Carried` ticks DOWN from 2 to 1 to 0. For each one,
   `Garden` ticks UP by 1 and `Score` ticks up by 1.
4. Final score telemetry reads 2.

## The pattern worth noticing

Both halves of your program have the same shape:

```java
mechanism.setPower(somePower);
while (opModeIsActive() && !conditionWeWant()) {
    telemetry.update();
    idle();
}
mechanism.setPower(0);
```

That shape — **start the thing, wait for the sensor to confirm, stop
the thing** — is the one you'll use again for the shooter in Lesson 7.
It's also exactly how the pros wrote `BioBuzzStarterbotTeleopMecanum`.

## Change one thing — predict first

- Switch the target to the red garden `(60, -60)`. Predict Jimothy's
  path, then run.
- Change the pickup target in your Lesson 4 code from the north flower
  `(0, 60)` to the east flower `(60, 0)`. Does the score end up the
  same?

Press **Next** when you've seen the garden score go up.
