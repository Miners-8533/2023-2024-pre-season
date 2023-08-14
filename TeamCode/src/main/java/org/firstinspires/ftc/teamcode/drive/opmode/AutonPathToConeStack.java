package org.firstinspires.ftc.teamcode.drive.opmode;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;


@Disabled
@Autonomous
public class AutonPathToConeStack extends LinearOpMode {
    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        // declare start position of 0x, 0y, 0 "heading" or direction,
        // this is _technically_ supposed to be center of the field
        Pose2d startPose = new Pose2d(0, 0, Math.toRadians(0));

        // reset robot position to startPose
        drive.setPoseEstimate(startPose);

        Trajectory testTrajectory = drive.trajectoryBuilder(startPose)
                // from starting tile F5, spline to cone stack w/o changing robot orientation,
                // this specific spline did work, however is not ideal as robot made contact with several junctions
                //.splineToConstantHeading(new Vector2d(56, -18), Math.toRadians(0))

                //from starting tile F5, drive straight forward _and_ interpolate 0 to 90 degrees clockwise
                .lineToLinearHeading(new Pose2d(56, 0, Math.toRadians(-90)))
                .build();

        waitForStart();

        if(isStopRequested()) return;

        drive.followTrajectory(testTrajectory);

    }
}
