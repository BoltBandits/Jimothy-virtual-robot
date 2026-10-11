**Goal:** drive Jimothy to the north flower, run the intake until it
holds 2 pollen, then stop. Verify with the HUD.

## The pickup routine

Replace the body of `runOpMode()` (everything between `resetPose()` and
the end) with this:

```java
// Head to the north flower
driveToTarget(0, 60);
turnToFace(0, 72);           // make sure we're pointing INTO the flower
telemetry.addLine("At north flower; starting intake");
telemetry.update();

// Spin the intake and wait for the scoreboard to say we've got 2 pollen
intake.setPower(0.6);
int targetCarry = 2;
while (opModeIsActive() && BioBuzzScoreboard.getPollenCarried() < targetCarry) {
    telemetry.addData("Pollen carried",
            BioBuzzScoreboard.getPollenCarried() + " / " + targetCarry);
    telemetry.update();
    idle();
}
intake.setPower(0);
telemetry.addLine("Got what we came for.");
telemetry.update();
sleep(2000);
```

## What you should see

Run **MyBioBuzzLab** against **Jimothy Bot**:

1. Jimothy drives from `(0, 0)` to `(0, 60)` — the north flower.
2. Jimothy spins to face the flower center.
3. The HUD's `Carried` count rises from 0 to 2.
4. Jimothy stops. The HUD's `Score` HAS NOT changed — we haven't
   deposited anything yet. We'll do that in a future lesson.

## Why we check the scoreboard, not time

A tempting way to write this would be:

```java
intake.setPower(0.6);
sleep(1000);                // "about 2 pollen's worth of time"
intake.setPower(0);
```

Don't. Three reasons:

1. If Jimothy isn't quite inside the flower circle (bad pose, bump at
   start), `sleep(1000)` runs the intake uselessly and doesn't pick up
   anything.
2. The scoreboard knows what actually happened. `sleep` doesn't.
3. On the real robot you'll replace the scoreboard check with a **beam
   break sensor** that fires when a ball enters the hopper. Same shape
   of code — different source of truth.

The general rule: **wait for the thing you actually want, not for an
amount of time**. The simulator teaches that habit cheaply.

## Change one thing — predict first

- Change `targetCarry = 2` to `targetCarry = 7`. The scoreboard caps at
  5. What do you predict the loop does? Run and check.
- Change `driveToTarget(0, 60)` to `driveToTarget(60, 0)` and
  `turnToFace(72, 0)`. What score do you expect from the east flower?
  (Hint: same points, different flower — the HUD does not care which
  one.)

That's slice 1 of BioBuzz Lab. The next few lessons will add:
- carry limits + ejecting into the garden,
- a hive aim + shooter spin-up gate,
- stringing a full autonomous match together.
