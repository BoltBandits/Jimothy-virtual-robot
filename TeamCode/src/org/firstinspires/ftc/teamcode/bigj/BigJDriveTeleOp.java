package org.firstinspires.ftc.teamcode.bigj;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Big J Drive", group = "Big J")
public class BigJDriveTeleOp extends OpMode {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, BigJConstants.FRONT_LEFT);
        frontRight = hardwareMap.get(DcMotor.class, BigJConstants.FRONT_RIGHT);
        backLeft = hardwareMap.get(DcMotor.class, BigJConstants.BACK_LEFT);
        backRight = hardwareMap.get(DcMotor.class, BigJConstants.BACK_RIGHT);
        frontLeft.setDirection(BigJConstants.LEFT_DIRECTION);
        backLeft.setDirection(BigJConstants.LEFT_DIRECTION);
        frontRight.setDirection(BigJConstants.RIGHT_DIRECTION);
        backRight.setDirection(BigJConstants.RIGHT_DIRECTION);
        for (DcMotor motor : new DcMotor[] {frontLeft, frontRight, backLeft, backRight}) {
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        telemetry.addLine("Left stick: drive and strafe. Right stick: turn.");
    }

    @Override
    public void loop() {
        double forward = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double turn = gamepad1.right_stick_x;
        double scale = Math.max(1, Math.abs(forward) + Math.abs(strafe) + Math.abs(turn));
        frontLeft.setPower((forward + strafe + turn) / scale);
        frontRight.setPower((forward - strafe - turn) / scale);
        backLeft.setPower((forward - strafe + turn) / scale);
        backRight.setPower((forward + strafe - turn) / scale);
    }

    @Override
    public void stop() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }
}
