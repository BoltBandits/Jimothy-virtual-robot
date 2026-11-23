package org.firstinspires.ftc.teamcode.bigj;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.api.Paths;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;

@Autonomous(name = "Big J Pedro Straight Line", group = "Big J")
public class BigJPedroAuto extends LinearOpMode {
    private static final double DISTANCE_INCHES = 12;
    private static final double TIMEOUT_SECONDS = 10;

    @Override
    public void runOpMode() throws InterruptedException {
        MecanumConfig drivetrain = new MecanumConfig(config -> {
            config.frontLeftName.set(BigJConstants.FRONT_LEFT);
            config.frontRightName.set(BigJConstants.FRONT_RIGHT);
            config.backLeftName.set(BigJConstants.BACK_LEFT);
            config.backRightName.set(BigJConstants.BACK_RIGHT);
            config.frontLeftDirection.set(BigJConstants.LEFT_DIRECTION);
            config.backLeftDirection.set(BigJConstants.LEFT_DIRECTION);
            config.frontRightDirection.set(BigJConstants.RIGHT_DIRECTION);
            config.backRightDirection.set(BigJConstants.RIGHT_DIRECTION);
            config.manualBrakeMode.set(true);
        });
        // The virtual Pinpoint supplies pose while Big J's three-wheel layout is being designed.
        Follower follower = new Follower(
                new PinpointLocalizer(hardwareMap, Constants.localizerConfig),
                new Mecanum(hardwareMap, drivetrain),
                new Foresight(Constants.foresightConfig));
        Pose start = new Pose(0, 0, 0);
        Pose goal = new Pose(DISTANCE_INCHES, 0, 0);
        Path path = Paths.line(start, goal).constant(start);

        telemetry.addLine("Place Big J with 12 inches of clearance ahead.");
        telemetry.update();
        waitForStart();
        if (isStopRequested()) return;

        follower.setPose(start);
        follower.update();
        ElapsedTime timer = new ElapsedTime();
        double elapsed;
        try {
            follower.follow(path);
            while (opModeIsActive() && follower.isBusy() && timer.seconds() < TIMEOUT_SECONDS) {
                follower.update();
                idle();
            }
            elapsed = timer.seconds();
        } finally {
            follower.stop();
            follower.drivetrain.stop(true);
        }

        Pose finish = follower.pose();
        while (opModeIsActive()) {
            telemetry.addData("Status", elapsed >= TIMEOUT_SECONDS ? "Timed out" : "Follower finished");
            telemetry.addData("Pose (in, deg)", "%.2f, %.2f, %.1f",
                    finish.x(), finish.y(), Math.toDegrees(finish.heading()));
            telemetry.addData("Endpoint error (in)", "%.2f",
                    Math.hypot(goal.x() - finish.x(), goal.y() - finish.y()));
            telemetry.addData("Elapsed (s)", "%.2f", elapsed);
            telemetry.update();
            idle();
        }
    }
}
