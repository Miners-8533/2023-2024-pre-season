package org.firstinspires.ftc.teamcode.drive.opmode.testopmodes.turretmotionplantests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.csa.miners.motionplan.MotionPlan;
import org.csa.miners.Turret;
import org.csa.miners.TurretFactory;
import org.csa.miners.TurretPosition;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class MotionPlanTestOpmode extends LinearOpMode {

    public Turret turret;
    public MotionPlan plan;

    TurretPosition loadPosition;
    TurretPosition unloadPosition;

    abstract public MotionPlan getMotionPlan(Turret turret);

    @Override
    public void runOpMode() {

        telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);

        turret = TurretFactory.getTurretFromHardwareMap(hardwareMap);
        plan = this.getMotionPlan(turret);

        // Wait for the start button
        telemetry.addData(">", "Press Start to scan Servo." );
        telemetry.update();

        waitForStart();


        // Scan servo till stop pressed.
        while(opModeIsActive()) {

            if (plan.isPlanInProgress() && plan.isPlanComplete()) {
                plan.resetPlan();
            } else if (plan.isPlanInProgress()) {
                plan.advance();
            } else {
                plan.start();
                plan.advance();
            }

//            turret.updateMovements();

            turret.reportTelemetry(telemetry);

            telemetry.addData(">","Plan Step %d of %d", plan.getCurrentStepIndex(), plan.getTotalSteps());
            telemetry.addData("Extension Arm", turret.extensionArm.getDirection());
            telemetry.addData("Current", turret.turretRotate.getCurrentPosition());
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
