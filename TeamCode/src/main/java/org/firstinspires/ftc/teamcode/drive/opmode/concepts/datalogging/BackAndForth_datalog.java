package org.firstinspires.ftc.teamcode.drive.opmode.concepts.datalogging;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.opmode.concepts.datalogging.Datalogger;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Op mode for preliminary tuning of the follower PID coefficients (located in the drive base
 * classes). The robot drives back and forth in a straight line indefinitely. Utilization of the
 * dashboard is recommended for this tuning routine. To access the dashboard, connect your computer
 * to the RC's WiFi network. In your browser, navigate to https://192.168.49.1:8080/dash if you're
 * using the RC phone or https://192.168.43.1:8080/dash if you are using the Control Hub. Once
 * you've successfully connected, start the program, and your robot will begin moving forward and
 * backward. You should observe the target position (green) and your pose estimate (blue) and adjust
 * your follower PID coefficients such that you follow the target position as accurately as possible.
 * If you are using SampleMecanumDrive, you should be tuning TRANSLATIONAL_PID and HEADING_PID.
 * If you are using SampleTankDrive, you should be tuning AXIAL_PID, CROSS_TRACK_PID, and HEADING_PID.
 * These coefficients can be tuned live in dashboard.
 *
 * This opmode is designed as a convenient, coarse tuning for the follower PID coefficients. It
 * is recommended that you use the FollowerPIDTuner opmode for further fine tuning.
 */


@Config
@Autonomous(group = "drive")
public class BackAndForth_datalog extends LinearOpMode {

    Datalog datalog;

    public static double DISTANCE = 50;

    @Override
    public void runOpMode() throws InterruptedException {

        datalog = new Datalog();

        int loop_count = 0;

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        Trajectory trajectoryForward = drive.trajectoryBuilder(new Pose2d())
                .forward(DISTANCE)
                .build();

        Trajectory trajectoryBackward = drive.trajectoryBuilder(trajectoryForward.end())
                .back(DISTANCE)
                .build();

        waitForStart();

        while (opModeIsActive() && !isStopRequested() && loop_count < 10) {
            loop_count ++;
            Pose2d poseEstimate = drive.getPoseEstimate();
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());
            telemetry.addData("loop count", loop_count);

            datalog.pose_est_x.set(poseEstimate.getX());
            datalog.pose_est_y.set(poseEstimate.getY());
            datalog.pose_est_heading.set(poseEstimate.getHeading());

            drive.followTrajectory(trajectoryForward);
            drive.followTrajectory(trajectoryBackward);
            telemetry.update();
            datalog.writeLine();
        }
    }
    public static class Datalog
    {
        // The underlying datalogger object - it cares only about an array of loggable fields
        private final Datalogger datalogger;

        // These are all of the fields that we want in the datalog.
        // Note that order here is NOT important. The order is important in the setFields() call below
        public Datalogger.GenericField opModeStatus = new Datalogger.GenericField("OpModeStatus");
        public Datalogger.GenericField loopCounter  = new Datalogger.GenericField("Loop Counter");
        public Datalogger.GenericField yawDegrees       = new Datalogger.GenericField("Yaw Angular Degrees");
        public Datalogger.GenericField yawVelo       = new Datalogger.GenericField("Yaw Velocity");
        public Datalogger.GenericField battery      = new Datalogger.GenericField("Battery");

        public Datalogger.GenericField pose_est_x = new Datalogger.GenericField("pose estimate X");
        public Datalogger.GenericField pose_est_y = new Datalogger.GenericField("pose estimate Y");
        public Datalogger.GenericField pose_est_heading = new Datalogger.GenericField("pose estimate heading");



        public Datalog()
        {
            // Build the underlying datalog object
            String fileName = new SimpleDateFormat("yyyyMMddHHmm'.csv'").format(new Date());
            datalogger = new Datalogger.Builder()

                    // Pass through the filename
                    .setFilename(fileName)

                    // Request an automatic timestamp field
                    .setAutoTimestamp(Datalogger.AutoTimestamp.DECIMAL_SECONDS)

                    // Tell it about the fields we care to log.
                    // Note that order *IS* important here! The order in which we list
                    // the fields is the order in which they will appear in the log.
                    .setFields(
                            pose_est_x,
                            pose_est_y,
                            pose_est_heading
                    )
                    .build();
        }

        // Tell the datalogger to gather the values of the fields
        // and write a new line in the log.
        public void writeLine()
        {
            datalogger.writeLine();
        }
    }
}