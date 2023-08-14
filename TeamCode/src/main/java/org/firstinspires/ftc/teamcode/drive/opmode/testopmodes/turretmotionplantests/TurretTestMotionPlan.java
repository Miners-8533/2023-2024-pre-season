package org.firstinspires.ftc.teamcode.drive.opmode.testopmodes.turretmotionplantests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.csa.miners.motionplan.MotionPlan;
import org.csa.miners.Turret;
import org.csa.miners.TurretFactory;
import org.csa.miners.TurretPosition;
import org.csa.miners.motionplan.WaitElement;
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
@Autonomous(name="TT: Motion Plan - UOID", group="Turret Test")
public class TurretTestMotionPlan extends LinearOpMode {

    Turret turret;

    MotionPlan plan;

    TurretPosition loadPosition;
    TurretPosition unloadPosition;

    @Override
    public void runOpMode() {

        telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);

        turret = TurretFactory.getTurretFromHardwareMap(hardwareMap);

        loadPosition = new TurretPosition(0,0, 0, 1);
        unloadPosition = new TurretPosition(50,-750, 150, 0);


//        plan = new AutonPlan(turret, loadPosition, unloadPosition);
        plan = new MotionPlan(turret);
        plan.add(new TurretPosition(0, -200, 0, 0));
        plan.add(new TurretPosition(0, -200, 200, 0));
        plan.add(new TurretPosition(500, -200, 200, 0));
        plan.add(new WaitElement(1000));
        plan.add(new TurretPosition(500, -200, 200, 1));
        plan.add(new TurretPosition(0, -200, 200, 1));
        plan.add(new TurretPosition(0, -200, 0, 1));
        plan.add(new TurretPosition(0, 0, 0, 1));
        plan.add(new TurretPosition(0, 0, 0, 0));

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


//            turret.moveTo(loadPosition = new TurretPosition(50,0, 0, 1));
                if (plan.isPlanInProgress() && plan.isPlanComplete()) {
                    // Do Nothing
                } else if (plan.isPlanInProgress()) {
                    plan.advance();
                } else {
                    plan.start();
                    plan.advance();
                }
//            turret.updateMovements();

            turret.reportTelemetry(telemetry);

            reportPosition("L", loadPosition);
            reportPosition("U", unloadPosition);

//            telemetry.addData("Targeted", target);
//            telemetry.addData("Power", power);
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
