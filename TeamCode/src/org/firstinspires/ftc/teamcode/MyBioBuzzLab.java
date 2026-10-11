package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import virtual_robot.games.BioBuzzScoreboard;

/**
 * BioBuzz Lab working file.
 *
 * This file grows across Lessons 2-10 of the BioBuzz Lab sub-course. Each
 * lesson adds ONE new idea on top of the previous one. Keep the config
 * block at the top in sync with the real robot's starter_bot.xml.
 */
@TeleOp(name = "My BioBuzz Lab", group = "BioBuzz Lab")
public class MyBioBuzzLab extends LinearOpMode {

    // ---------------- CONFIG BLOCK (edit here if wiring changes) --------
    private static final String LEFT_FRONT  = "left_front_drive";
    private static final String RIGHT_FRONT = "right_front_drive";
    private static final String LEFT_BACK   = "left_back_drive";
    private static final String RIGHT_BACK  = "right_back_drive";
    private static final String LEFT_POD    = "intake_motor";
    private static final String RIGHT_POD   = "odo_right";
    private static final String CENTER_POD  = "odo_center";

    private static final double LEFT_TPI        = 509.66;
    private static final double RIGHT_TPI       = 507.60;
    private static final double CENTER_TPI      = 507.92;
    private static final double POD_SPACING_IN  = 12.263;
    private static final double CENTER_POD_X_IN = -6.718;

    // ---------------- hardware references (filled in init) --------------
    private DcMotor fl, fr, bl, br;
    private DcMotor leftPod, rightPod, centerPod;

    // ---------------- pose state ---------------------------------------
    private int prevLeft, prevRight, prevCenter;
    private double x, y, heading;

    @Override
    public void runOpMode() {
        fl = hardwareMap.get(DcMotor.class, LEFT_FRONT);
        fr = hardwareMap.get(DcMotor.class, RIGHT_FRONT);
        bl = hardwareMap.get(DcMotor.class, LEFT_BACK);
        br = hardwareMap.get(DcMotor.class, RIGHT_BACK);
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        for (DcMotor m : new DcMotor[]{fl, fr, bl, br}) {
            m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            m.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        leftPod   = hardwareMap.get(DcMotor.class, LEFT_POD);
        rightPod  = hardwareMap.get(DcMotor.class, RIGHT_POD);
        centerPod = hardwareMap.get(DcMotor.class, CENTER_POD);
        for (DcMotor p : new DcMotor[]{leftPod, rightPod, centerPod}) {
            p.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        resetPose();

        telemetry.addLine("MyBioBuzzLab ready");
        telemetry.update();
        waitForStart();
        if (isStopRequested()) return;
        resetPose();

        // TODO (Lesson 2): replace this block with a drive-to-target loop
        //                   that reaches targetX = 24, targetY = 0.
        while (opModeIsActive()) {
            updatePose();
            telemetry.addData("Pose", "(%+.1f, %+.1f) @ %+.1f deg",
                    x, y, Math.toDegrees(heading));
            telemetry.addData("Pollen carried", BioBuzzScoreboard.getPollenCarried());
            telemetry.addData("Score",         BioBuzzScoreboard.getAllianceScore());
            telemetry.update();
            idle();
        }
    }

    // ================================================================== pose
    private void resetPose() {
        prevLeft   = -leftPod.getCurrentPosition();
        prevRight  =  rightPod.getCurrentPosition();
        prevCenter = -centerPod.getCurrentPosition();
        x = 0; y = 0; heading = 0;
    }

    private void updatePose() {
        int leftTicks   = -leftPod.getCurrentPosition();
        int rightTicks  =  rightPod.getCurrentPosition();
        int centerTicks = -centerPod.getCurrentPosition();

        double dL = (leftTicks   - prevLeft)   / LEFT_TPI;
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

    private static double normalize(double rad) {
        while (rad >  Math.PI) rad -= 2 * Math.PI;
        while (rad <= -Math.PI) rad += 2 * Math.PI;
        return rad;
    }

    // ================================================================== drive
    protected void setMecanum(double forward, double strafe, double turn) {
        double a = forward + strafe + turn;
        double b = forward - strafe - turn;
        double c = forward - strafe + turn;
        double d = forward + strafe - turn;
        double scale = Math.max(1.0, Math.max(Math.max(Math.abs(a), Math.abs(b)),
                Math.max(Math.abs(c), Math.abs(d))));
        fl.setPower(Range.clip(a / scale, -1, 1));
        fr.setPower(Range.clip(b / scale, -1, 1));
        bl.setPower(Range.clip(c / scale, -1, 1));
        br.setPower(Range.clip(d / scale, -1, 1));
    }
}
