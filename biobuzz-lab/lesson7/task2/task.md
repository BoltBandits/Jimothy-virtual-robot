**Goal:** grab the shooter and windmill, add a `launchOne` method.

## Imports and hardware

Near the other imports at the top of `MyBioBuzzLab.java`, add:

```java
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
```

Below the `intake` reference you added in Lesson 4, add two more:

```java
DcMotorEx shooter  = hardwareMap.get(DcMotorEx.class, "shooter_motor");
CRServo   windmill = hardwareMap.get(CRServo.class,   "windmill_servo");
```

Note: in Lesson 4 you got `intake` as `DcMotor.class`. For the shooter
we ask for `DcMotorEx.class` so we get the velocity-control methods.
The real `shooter_motor` on Jimothy supports both — it is a goBILDA 5202
with encoder.

## The method

Put this method next to `turnToFace`:

```java
private static final double SHOOTER_TARGET_VELOCITY = 300.0;   // ticks/sec
private static final double SHOOTER_READY_TOLERANCE = 15.0;
private static final double SHOOTER_SPINUP_TIMEOUT_SEC = 2.0;

private void launchOne(DcMotorEx shooter, CRServo windmill) {
    // 1. Start the flywheel
    shooter.setVelocity(SHOOTER_TARGET_VELOCITY);

    // 2. Wait for it to be at speed (or time out)
    long deadlineNs = System.nanoTime()
            + (long)(SHOOTER_SPINUP_TIMEOUT_SEC * 1_000_000_000L);
    while (opModeIsActive() && System.nanoTime() < deadlineNs) {
        double v = shooter.getVelocity();
        telemetry.addData("Spin-up", "%.0f / %.0f ticks/s",
                v, SHOOTER_TARGET_VELOCITY);
        telemetry.update();
        if (Math.abs(v - SHOOTER_TARGET_VELOCITY) <= SHOOTER_READY_TOLERANCE) break;
        idle();
    }

    // 3. Feed one ball through the windmill. Watch the scoreboard; stop
    //    as soon as `pollenInHive` goes up by 1.
    int before = BioBuzzScoreboard.getPollenInHive();
    windmill.setPower(-1.0);
    while (opModeIsActive()
            && BioBuzzScoreboard.getPollenInHive() == before) {
        telemetry.addData("Shooter", "%.0f ticks/s", shooter.getVelocity());
        telemetry.addData("Hive total", BioBuzzScoreboard.getPollenInHive());
        telemetry.update();
        idle();
    }
    windmill.setPower(0);
}
```

Three things worth saying out loud:

1. **The spin-up has a timeout.** If something is wrong with the shooter
   (motor unplugged in real life), the method shouldn't hang forever.
   `deadlineNs` plus the timeout check is the standard idiom.
2. **The feed watches the scoreboard, not the clock.** Same lesson as
   Lesson 4: wait for the thing you actually want.
3. **`before` captures state before the action.** A common pattern:
   "do thing, wait for the counter to change." Works for pollen, hits
   taken in a game, API calls succeeded, anything.

Press **Next** to score your first hive point.
