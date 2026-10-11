**Goal:** make Jimothy spin in place until it is pointed at a flower.
**You'll learn:** heading control — the same proportional idea as Lesson
2, but on a wrap-around number (an angle).

## Why heading matters

To pick up pollen, Jimothy has to be inside a flower zone **and** running
its intake. But to *launch* into the hive later, Jimothy also has to
**face the hive**, because the shooter only throws pollen in one
direction (straight out the front). So we need a way to say "turn until
you are pointing at `(targetX, targetY)`."

The target bearing — the angle from Jimothy to the target point — is
one math call:

```java
double targetBearing = Math.atan2(targetY - y, targetX - x);
```

The heading error is `targetBearing - heading`. We want to turn until
that error is close to 0.

## The wrap-around trap

Angles are tricky. Say Jimothy is at `heading = 170°` and the target
bearing is `-170°`. The naive error is `-170 - 170 = -340°`. But the
real shortest turn is **only 20°**, in the other direction.

Any angle math you write needs to normalize to the range `(-180°, 180°]`
(or `(-π, π]` in radians). `MyBioBuzzLab.java` already has a
`normalize(double rad)` helper from Lesson 2. Use it.

Press **Next** to add the turn-to-face method.
