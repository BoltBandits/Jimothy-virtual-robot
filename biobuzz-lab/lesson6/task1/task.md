**Goal:** write a method that answers one question — **is Jimothy ready
to shoot into the hive?**
**You'll learn:** turning several measurements into one `boolean`. The
same pattern is behind every "lock-on" in every FTC autonomous.

## Three things have to be true

The game's launch rule looks like this (from `BioBuzzGame.java`):

```java
boolean readyToLaunch = HIVE.contains(botXm, botYm)
        && shooterTicks >= SHOOTER_READY_TICKS
        && windmillPower <= WINDMILL_FEED;
```

Three conditions, joined with `&&`:

1. **Position** — Jimothy's center is inside the hive zone (within ~0.5 m
   of field center).
2. **Shooter speed** — the shooter motor's actual velocity is at or
   above the target (150 ticks/sec in the sim).
3. **Feed** — the windmill is running in the feed direction.

Of those three, your code is responsible for #2 and #3. The game only
*checks* them. In this lesson we focus on #1 — the geometry — and build
a boolean that tells you *when the aim is good*.

## The two numbers

To know if Jimothy can launch into the hive, your code needs:

- **Distance to hive**: `Math.hypot(0 - x, 0 - y)` — the hive is at
  `(0, 0)` in field coordinates.
- **Heading error to hive**: `atan2(0 - y, 0 - x) - heading`,
  normalized.

If both are within tolerance, we are "aimed in." That is the whole idea.

Press **Next** to add the `readyToLaunch` method.
