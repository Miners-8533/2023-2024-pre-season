package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

/**
 * This is a simple teleop routine for testing localization. Drive the robot around like a normal
 * teleop routine and make sure the robot's estimated pose matches the robot's actual pose (slight
 * errors are not out of the ordinary, especially with sudden drive motions). The goal of this
 * exercise is to ascertain whether the localizer has been configured properly (note: the pure
 * encoder localizer heading may be significantly off if the track width has not been tuned).
 */

@Disabled
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(group = "TeleOp")
public class TeleOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        //drive is declared in the SampleMecanumDrive
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        /** below declarations are for the Expansion Hub 2
        private DcMotor elevator1;
        private DcMotor elevator2;
        private DcMotor extender;

        elevator1 = hardwareMap.get(DcMotor.class, "elevator1");
        elevator1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        elevator1.setDirection(DcMotor.Direction.FORWARD);

        elevator2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        elevator2 = hardwareMap.get(DcMotor.class, "elevator2");
        elevator2.setDirection(DcMotor.Direction.REVERSE);

        extender = hardwareMap.get(DcMotor.class, "extender");
        extender.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //extender.setDirection(DcMotor.Direction.REVERSE); //only use when reverse mode is needed
        */


        waitForStart();

        while (!isStopRequested()) {
            drive.setWeightedDrivePower(
                    new Pose2d(
                            -gamepad1.left_stick_y,
                            -gamepad1.left_stick_x,
                            -gamepad1.right_stick_x
                    )
            );

            drive.update();

            Pose2d poseEstimate = drive.getPoseEstimate();
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());
            telemetry.update();
        }

        //while (!isStopRequested()) {

            //telemetry.addData("x", poseEstimate.getX());
            //telemetry.update();
        //}

    }
}
