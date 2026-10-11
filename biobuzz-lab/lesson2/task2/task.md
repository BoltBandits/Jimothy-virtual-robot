**Goal:** start a new OpMode that reads Jimothy's three odometry pods and
prints the pose to telemetry.

## Open the starter

The working file is already in place at
`TeamCode/src/org/firstinspires/ftc/teamcode/MyBioBuzzLab.java`
(a reference copy also lives in this lesson's `starter-code/` folder
in case you need to start over).

Open it in IntelliJ. Look at the top block of the file — the **config
block** — and notice how it matches Jimothy's hardware names from Lesson 1:

```java
private static final String LEFT_FRONT  = "left_front_drive";
private static final String RIGHT_FRONT = "right_front_drive";
private static final String LEFT_BACK   = "left_back_drive";
private static final String RIGHT_BACK  = "right_back_drive";
private static final String LEFT_POD    = "intake_motor";
private static final String RIGHT_POD   = "odo_right";
private static final String CENTER_POD  = "odo_center";
```

Same ritual as the main course: one block at the top you can edit if the
real robot's wiring changes. Everything below should never need to care
what the hardware is called.

## The pose math

Below the config block, there's a method called `updatePose()`. **This
is the only new idea in this lesson.** Read the comments carefully:

```java
private void updatePose() {
    int leftTicks   = -leftPod.getCurrentPosition();   // NEGATED: see Lesson 1
    int rightTicks  =  rightPod.getCurrentPosition();
    int centerTicks = -centerPod.getCurrentPosition();

    double dL = (leftTicks   - prevLeft)   / LEFT_TPI;      // inches since last tick
    double dR = (rightTicks  - prevRight)  / RIGHT_TPI;
    double dC = (centerTicks - prevCenter) / CENTER_TPI;
    prevLeft = leftTicks;
    prevRight = rightTicks;
    prevCenter = centerTicks;

    double dHeading = (dR - dL) / POD_SPACING_IN;
    double forward  = (dL + dR) / 2.0;
    double lateral  = dC - CENTER_POD_X_IN * dHeading;
    double mid = heading + dHeading / 2.0;
    x += forward * Math.cos(mid) - lateral * Math.sin(mid);
    y += forward * Math.sin(mid) + lateral * Math.cos(mid);
    heading = normalize(heading + dHeading);
}
```

Three things to notice:

1. **Negation.** The left and center pod cables are wired "backwards" on
   the physical Jimothy, so we flip their sign in code. If you change the
   wiring, you change these negations — not the formulas below.
2. **`LEFT_TPI = 509.66`, `RIGHT_TPI = 507.60`, `CENTER_TPI = 507.92`.**
   These are **ticks per inch** — someone measured them by pushing
   Jimothy forward exactly 48 inches and reading the encoders. If they're
   wrong, Jimothy will think it moved further or less than it actually
   did.
3. **Dead reckoning.** This method only knows *change*. If Jimothy picks
   its wheels up off the ground and you carry it somewhere, the code
   will have no idea.

## Run it

Build and run the sim. Pick **MyBioBuzzLab** in the Op Mode dropdown. INIT,
START, then drive around with the on-screen gamepad. Watch the pose on
telemetry — it should change like you'd expect:

- Drive forward: `x` goes positive.
- Strafe left: `y` goes positive.
- Turn CCW: `heading` goes positive (in degrees).

Press **Next** to make Jimothy drive to a pose on its own.
