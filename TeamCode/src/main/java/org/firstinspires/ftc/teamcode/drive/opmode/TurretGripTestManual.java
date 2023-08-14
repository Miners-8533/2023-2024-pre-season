package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import static com.qualcomm.robotcore.hardware.DcMotor.Direction;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * This OpMode scans a single servo back and forward until Stop is pressed.
 * The code is structured as a LinearOpMode
 * INCREMENT sets how much to increase/decrease the servo position each cycle
 * CYCLE_MS sets the update period.
 *
 * This code assumes a Servo configured with the name "left_hand" as is found on a Robot.
 *
 * NOTE: When any servo position is set, ALL attached servos are activated, so ensure that any other
 * connected servos are able to move freely before running this test.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Disabled
@TeleOp(name = "Turret Test: Manual", group = "Turret Test")
public class TurretGripTestManual extends LinearOpMode {

    static final double INCREMENT   = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final int    END_PAUSE   = 1000;
    static final int    CYCLE_MS    =   50;     // period of each cycle
    //static final double MAX_POS     =  1.0;     // Maximum rotational position
    static final double MAX_POS     =  0.8;     // Maximum rotational position
    static final double MIN_POS     =  0.0;     // Minimum rotational position
    static final double EXTENSION_POWER = 0.05;

    // Define class members
    Servo servo;
    DcMotor extensionArm;
    DcMotor liftLeft;
    DcMotor liftRight;

    double position = 0;
    Gamepad turretPad = gamepad1;
    double extendPower;
    double liftPower;
    double liftLeftPower;
    double liftRightPower;

    /** The Left Lift power adjustment to ensure that the sides remain aligned */
    double LIFT_LEFT_POWER_ADJUST  = 0.25;
    /** The Right Lift power adjustment to ensure that the sides remain aligned */
    double LIFT_RIGHT_POWER_ADJUST = 0.25;

    @Override
    public void runOpMode() {

        // =========================================================================================
        // Initialization Code
        // This code belongs in the init() method but this is already defined as final.
        // Need to move this to a more appropriate class
        // =========================================================================================

        servo = hardwareMap.get(Servo.class, "gripper_servo");
        extensionArm  = hardwareMap.get(DcMotor.class, "extension_motor");
        liftLeft  = hardwareMap.get(DcMotor.class, "lift_left");
        liftRight  = hardwareMap.get(DcMotor.class, "lift_right");

        liftLeft.setDirection(Direction.FORWARD);
        liftRight.setDirection(Direction.REVERSE);
        extensionArm.setDirection(Direction.FORWARD);
        extensionArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Gamepad turretPad = gamepad2;

        // Wait for the start button
        telemetry.addData(">", "Press Start to scan Servo." );
        telemetry.update();
        waitForStart();


        // Scan servo till stop pressed.
        while(opModeIsActive()){

            int ONE_MOTOR_ROTATION = 538;
            int ONE_PULLY_ROTATION = ONE_MOTOR_ROTATION*20;

            if (turretPad.a){
                servo.setPosition(1);
            }

            if (turretPad.b){
                servo.setPosition(0);
            }

            extendPower = -turretPad.right_stick_y;
            extendPower *= .25; //reduces total power on stick
            extensionArm.setPower(extendPower);

            liftPower = -turretPad.left_stick_y;
            liftLeftPower  = liftPower * LIFT_LEFT_POWER_ADJUST; //reduces total power on stick
            liftRightPower = liftPower * LIFT_RIGHT_POWER_ADJUST; //reduces total power on stick
            liftLeft.setPower(liftLeftPower);
            liftRight.setPower(liftRightPower);


            telemetry.addData("Servo Position", "%5.2f", servo.getPosition());
            telemetry.addData("Arm Posititon", "%5d", extensionArm.getCurrentPosition());
            telemetry.addData("Buttons Pressed", "%s%s%s%s",
                    turretPad.a ? "a" : "",
                    turretPad.b ? "b" : "",
                    String.format("LSX%f0.3", turretPad.left_stick_x),
                    String.format("LSY%f0.3", turretPad.left_stick_y)
            );
            telemetry.addData("LL Position", "%5d",
                    liftLeft.getCurrentPosition());

            telemetry.addData(">", "Press Stop to end test." );
            telemetry.update();

            idle();
        }


        // Signal done;
        telemetry.addData(">", "Done");
        telemetry.update();
    }

    public void closeGripper() {
        servo.setPosition(0);
        writeTelemetryData();
    }

    public void openGripper() {
        servo.setPosition(1);
        writeTelemetryData();
    }

    public void writeTelemetryData(){
        telemetry.addData("Servo Position", "%5.2f",
                servo.getPosition());
        telemetry.addData("Arm Posititon", "%5d",
                extensionArm.getCurrentPosition());
        telemetry.addData(">", "Press Stop to end test." );
        telemetry.update();
    }

}
