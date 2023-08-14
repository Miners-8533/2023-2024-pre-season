package org.csa.miners.test.configuration;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.csa.miners.Turret;
import org.csa.miners.TurretFactory;
import org.csa.miners.TurretPosition;
import org.firstinspires.ftc.robotcore.external.Telemetry;

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
@Autonomous(name="Motor Testing: Gripper Cycle", group="Motor Tests")
public class TestGripper extends LinearOpMode {

    Turret turret;

    TurretPosition loadPosition;
    TurretPosition unloadPosition;

    @Override
    public void runOpMode() {

        telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);

        turret = TurretFactory.getTurretFromHardwareMap(hardwareMap);

        loadPosition = new TurretPosition(0,0, 0, 1);
        unloadPosition = new TurretPosition(50,-750, 150, 0);


//        plan = new AutonPlan(turret, loadPosition, unloadPosition);

        // Wait for the start button
        telemetry.addData(">", "Press Start to scan Servo." );
        telemetry.update();

        waitForStart();


        // Scan servo till stop pressed.
        while(opModeIsActive()){

//            turretRotationMotor.setTargetPosition(50);
//            int target = 50;
//            double power = turret.MAX_ROTATION_SPEED;
//            double delta = 2;
//            turretRotationMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//            if(Math.abs(target - turretRotationMotor.getCurrentPosition()) < delta) {
//                power = 0;
//            } else
//            if (target > turretRotationMotor.getCurrentPosition()) {
//                power = -turret.MAX_ROTATION_SPEED;
//            } else if (target < turretRotationMotor.getCurrentPosition()) {
//                power = turret.MAX_ROTATION_SPEED;
//            }
//
//            turretRotationMotor.setPower(power);

            int delay = 2000;

            telemetry.addLine("Closing Gripper");
            telemetry.update();
            turret.closeGripper();
            telemetry.addLine("Gripper Closed");
            telemetry.update();
            sleep(delay);

            telemetry.addLine("Opening Gripper");
            telemetry.update();
            turret.openGripper();
            telemetry.addLine("Gripper Open");
            telemetry.update();
            sleep(delay);

            telemetry.addLine("Closing Gripper");
            telemetry.update();
            turret.closeGripper();
            telemetry.addLine("Gripper Closed");
            telemetry.update();

            telemetry.addData(">", "Press Stop to end test." );
            telemetry.update();

            idle();
        }

        // Signal done;
        telemetry.addData(">", "Done");
        telemetry.update();

    }

    public void reportPosition(String caption, TurretPosition position) {
        telemetry.addData(caption, "%4.2f |%05d |      |%05d |%05d",
                position.grip(),
                position.elevation(),
                position.extension(),
                position.rotation()
        );
    }

}
