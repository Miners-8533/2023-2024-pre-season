package org.firstinspires.ftc.teamcode.drive.opmode.roadrunnertune;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.Localizer;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.StandardTrackingWheelLocalizer;
import org.firstinspires.ftc.teamcode.drive.opmode.concepts.datalogging.ConceptDatalogger;
import org.firstinspires.ftc.teamcode.util.Encoder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.firstinspires.ftc.teamcode.drive.opmode.concepts.datalogging.Datalogger;
/**
 * This is a simple teleop routine for testing localization. Drive the robot around like a normal
 * teleop routine and make sure the robot's estimated pose matches the robot's actual pose (slight
 * errors are not out of the ordinary, especially with sudden drive motions). The goal of this
 * exercise is to ascertain whether the localizer has been configured properly (note: the pure
 * encoder localizer heading may be significantly off if the track width has not been tuned).
 */


@Autonomous(group = "drive")
public class outputEncoderValues extends LinearOpMode {
    private Encoder leftEncoder, rightEncoder, frontEncoder;

    Datalog datalog;

    IMU imu;

    VoltageSensor battery;

    @Override
    public void runOpMode() throws InterruptedException {
        datalog = new Datalog();

        imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection  usbDirection  = RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;

        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);

        imu.initialize(new IMU.Parameters(orientationOnRobot));

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        imu.resetYaw();

        leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "leftFront"));
        rightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "rightFront"));
        frontEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "leftRear"));

        telemetry.setMsTransmissionInterval(50);

        int i = 0;
        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            i++;
            drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            drive.setWeightedDrivePower(
                    new Pose2d(
                            -gamepad1.left_stick_y,
                            -gamepad1.left_stick_x,
                            -gamepad1.right_stick_x
                    )
            );

            drive.update();
            datalog.loopCounter.set(i);
            // datalog parallel vs perpendicular dead wheel velo vs. imu velo

            datalog.lParPos.set(leftEncoder.getCurrentPosition());
            datalog.rParPos.set(rightEncoder.getCurrentPosition());
            datalog.perpPos.set(frontEncoder.getCurrentPosition());
            datalog.lParVelo.set(leftEncoder.getCorrectedVelocity());
            datalog.rParVelo.set(rightEncoder.getCorrectedVelocity());
            datalog.perpVelo.set(frontEncoder.getCorrectedVelocity());

            YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
            AngularVelocity angularVelocity = imu.getRobotAngularVelocity(AngleUnit.DEGREES);

            datalog.yawDegrees.set(orientation.getYaw(AngleUnit.DEGREES));
            datalog.yawVelo.set(angularVelocity.zRotationRate);
            datalog.battery.set(battery.getVoltage());
            datalog.writeLine();

            telemetry.addData("Left Encoder position", datalog.lParPos);
            telemetry.addData("Right Encoder position", datalog.rParPos);
            telemetry.addData("Front Encoder position", datalog.perpPos);
            telemetry.addData("Left encoder correct velocity", datalog.lParVelo);
            telemetry.addData("Right encoder correct velocity", datalog.rParVelo);
            telemetry.addData("Front encoder correct velocity", datalog.perpVelo);
            telemetry.addLine();
            telemetry.addData("Yaw (Z)", "%.2f Deg. (Heading)", datalog.yawDegrees);
            telemetry.addData("Yaw (Z) velocity", "%.2f Deg/Sec", datalog.yawVelo);

            telemetry.update();
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

        public Datalogger.GenericField lParPos = new Datalogger.GenericField("Left Encoder Position");
        public Datalogger.GenericField rParPos = new Datalogger.GenericField("Right Encoder Position");
        public Datalogger.GenericField perpPos = new Datalogger.GenericField("Perpendicular Encoder Position");
        public Datalogger.GenericField lParVelo = new Datalogger.GenericField("Left Encoder Corrected Velocity");
        public Datalogger.GenericField rParVelo = new Datalogger.GenericField("Right Encoder Corrected Velocity");
        public Datalogger.GenericField perpVelo = new Datalogger.GenericField("Perpendicular Encoder Corrected Velocity");

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
                            loopCounter,
                            lParPos,
                            lParVelo,
                            rParPos,
                            rParVelo,
                            perpPos,
                            perpVelo,
                            yawDegrees,
                            yawVelo,
                            battery
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
