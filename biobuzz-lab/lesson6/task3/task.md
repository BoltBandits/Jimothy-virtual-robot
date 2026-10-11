**Goal:** drive Jimothy to the hive, lock in the aim, and leave it there.

## Compose the moves

Replace the diagnostic loop with this:

```java
driveToTarget(HIVE_X, HIVE_Y);
turnToFace(HIVE_X - 1, HIVE_Y);  // face slightly past the hive (any point beyond)

while (opModeIsActive() && !aimedAtHive()) {
    telemetry.addLine("Not quite aimed yet...");
    telemetry.update();
    idle();
}

telemetry.addLine("Aim locked. Hold for 2 seconds.");
telemetry.update();
sleep(2000);
```

(Comment out your Lesson 4 and Lesson 5 code for this run — we just want
to see the aim lock behavior without the pickup/eject noise.)

## What you should see

1. Jimothy drives from start to the hive at the field center.
2. Jimothy spins to face the far side of the hive.
3. Telemetry prints "Aim locked. Hold for 2 seconds."

If instead you see "Not quite aimed yet..." forever, something is off.
Likely causes:

- You set a tolerance too tight (`HIVE_HEADING_TOL = 0.5°` won't finish
  on a bumpy sim).
- `turnToFace` is targeting a point ON Jimothy (`(0, 0)` when Jimothy
  is at `(0, 0)`), so `atan2` returns 0 and Jimothy never actually
  turns to face anything.

The first is a tolerance tuning problem. The second is why we aim at
`HIVE_X - 1`, not `HIVE_X`.

## Why this matters

Every scoring action in BIOBUZZ — flower place, hive launch — is a
drive move + an aim check + an action. You now have the first two.
Lesson 7 adds the launch action.

Press **Next**.
