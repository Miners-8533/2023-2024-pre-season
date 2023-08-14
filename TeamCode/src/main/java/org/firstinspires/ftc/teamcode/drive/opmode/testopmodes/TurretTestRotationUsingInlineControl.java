package org.firstinspires.ftc.teamcode.drive.opmode.testopmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.csa.miners.motionplan.MotionPlan;
import org.csa.miners.Turret;
import org.csa.miners.TurretFactory;
import org.csa.miners.TurretPosition;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * This OpMode proves that we can manually control the turret motor.  It proved out that the
 * GoToPosition method on DC Motors doesn't work for the Core Hex Motors
 */

@Disabled
@Autonomous(name="TT: Rotation - Hardcode", group="Turret Test")
public class TurretTestRotationUsingInlineControl extends LinearOpMode {

    DcMotor turretRotationMotor;

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

        // Wait for the start button
        telemetry.addData(">", "Press Start to scan Servo." );
        telemetry.update();

        waitForStart();


        // Scan servo till stop pressed.
        while(opModeIsActive()){

            turretRotationMotor.setTargetPosition(50);
            int target = 50;
            double power = turret.MAX_ROTATION_SPEED;
            double delta = 2;
            turretRotationMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            if(Math.abs(target - turretRotationMotor.getCurrentPosition()) < delta) {
                power = 0;
            } else
            if (target > turretRotationMotor.getCurrentPosition()) {
                power = -turret.MAX_ROTATION_SPEED;
            } else if (target < turretRotationMotor.getCurrentPosition()) {
                power = turret.MAX_ROTATION_SPEED;
            }

            turretRotationMotor.setPower(power);


            turret.reportTelemetry(telemetry);

            reportPosition("L", loadPosition);
            reportPosition("U", unloadPosition);

            telemetry.addData("Current", turretRotationMotor.getCurrentPosition());
            telemetry.addData("Targeted", target);
            telemetry.addData("Power", power);
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
