**Goal:** see Jimothy's hardware map and confirm it matches the real robot.

## The hardware

In the `Configuration` dropdown at the top of the sim window, make sure
**Jimothy Bot** is selected. In the top-left of the field you'll see the
score HUD.

Jimothy has four groups of hardware. Open
`Controller/src/virtual_robot/robots/classes/JimothyBot.java` and scroll
past the comment at the top. You will see this table in the code:

| Group | Names in `hardwareMap` |
| --- | --- |
| Mecanum drive | `left_front_drive`, `right_front_drive`, `left_back_drive`, `right_back_drive` |
| Mechanisms | `intake_motor`, `shooter_motor` |
| Odometry pods | `intake_motor` (left pod), `odo_right`, `odo_center` |
| Continuous servos | `left_intake_servo`, `right_intake_servo`, `windmill_servo` |
| IMU | `imu` |

Three things to notice:

1. **`intake_motor` is in two groups.** On the real Jimothy, the intake
   motor is wired to Expansion Hub port 0 — but the encoder cable from
   that port is plugged into a dead-wheel odometry pod. So when your code
   calls `intake_motor.setPower(0.3)`, the intake wheel spins; when it
   calls `intake_motor.getCurrentPosition()`, it reads the LEFT odometry
   pod's position. The simulator does the same thing.

2. **Direction matters.** The left-side drive motors are `REVERSE`,
   right-side are `FORWARD`. That is already written in the OpModes you'll
   port — do not change it without a reason.

3. **These names are copied from `starter_bot.xml`.** If the physical
   robot's hardware configuration changes, Jimothy's twin changes to
   match. Code moves between the two by copy-paste.

Press **Next** to run a program that uses this hardware.
