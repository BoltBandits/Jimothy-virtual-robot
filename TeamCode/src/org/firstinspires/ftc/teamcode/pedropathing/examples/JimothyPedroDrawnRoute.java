package org.firstinspires.ftc.teamcode.pedropathing.examples;

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
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.pedropathing.tuning.Constants;

@Autonomous(name = "Jimothy Pedro Drawn Route", group = "Jimothy")
public class JimothyPedroDrawnRoute extends LinearOpMode {
    private static final double TIMEOUT_SECONDS = 90;

    @Override
    public void runOpMode() throws InterruptedException {
        MecanumConfig drivetrain = new MecanumConfig(config -> {
            config.frontLeftName.set("left_front_drive");
            config.frontRightName.set("right_front_drive");
            config.backLeftName.set("left_back_drive");
            config.backRightName.set("right_back_drive");
            config.frontLeftDirection.set(Direction.REVERSE);
            config.backLeftDirection.set(Direction.REVERSE);
            config.frontRightDirection.set(Direction.FORWARD);
            config.backRightDirection.set(Direction.FORWARD);
            config.manualBrakeMode.set(true);
        });
        Follower follower = new Follower(
                new PinpointLocalizer(hardwareMap, Constants.localizerConfig),
                new Mecanum(hardwareMap, drivetrain),
                new Foresight(Constants.foresightConfig));
        // Points from path.pp.json, in field inches.
        Pose start = new Pose(55.813077939233814, 7.626155878467635, Math.PI / 2);
        Pose end1 = new Pose(56.186922060766186, 36.56076618229855, 0);
        Pose end2 = new Pose(22.150264200792602, 51.216644649933954, 0);
        Pose end3 = new Pose(26.449471598414796, 83.3672391017173, 0);
        Pose control1 = new Pose(103.89108750337874, 35.3843043739724, 0);
        Pose control2 = new Pose(17.50146672572638, 26.53903886600075, 0);
        Pose control3 = new Pose(38.19493271670356, 62.70314376920695, 0);
        Path path = Paths.path(
                Paths.curve(start, control1, end1).linear(Math.PI / 2, Math.PI),
                Paths.curve(end1, control2, end2).tangent(),
                Paths.curve(end2, control3, end3).tangent());
        Pose goal = path.endPose();

        telemetry.addLine("Ready: place Jimothy at (55.81, 7.63), facing upfield");
        telemetry.update();
        waitForStart();
        if (isStopRequested()) return;

        follower.setPose(start);
        follower.update();
        follower.follow(path);
        ElapsedTime timer = new ElapsedTime();

        try {
            while (opModeIsActive() && follower.isBusy() && timer.seconds() < TIMEOUT_SECONDS) {
                follower.update();
                showPose(follower.pose(), goal, "Following", timer.seconds());
                idle();
            }
        } finally {
            follower.stop();
            follower.drivetrain.stop(true);
        }

        String result = timer.seconds() >= TIMEOUT_SECONDS ? "Timed out" : "Follower finished";
        double timeout = timer.seconds();
        while (opModeIsActive()) {
            showPose(follower.pose(), goal, result, timeout);
            idle();
        }
    }

    private void showPose(Pose pose, Pose goal, String status, double elapsedSeconds) {
        telemetry.addData("Status", status);
        telemetry.addData("Pose (in, deg)", "x %.2f, y %.2f, heading %.1f",
                pose.x(), pose.y(), Math.toDegrees(pose.heading()));
        telemetry.addData("Position error (in)", "%.2f",
                Math.hypot(goal.x() - pose.x(), goal.y() - pose.y()));
        telemetry.addData("Time elapsed: ", "%.2f secs", elapsedSeconds);
        telemetry.update();
    }
}

