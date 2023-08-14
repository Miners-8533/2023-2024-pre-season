package org.csa.miners.test.configuration;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.csa.miners.motionplan.MotionPlan;
import org.csa.miners.Turret;
import org.csa.miners.TurretPosition;
import org.firstinspires.ftc.teamcode.drive.opmode.testopmodes.turretmotionplantests.MotionPlanTestOpmode;

@Disabled
@Autonomous(name="MotionPlan - Base Rotation w/ Lift", group="MotionPlan Tests")
public class TestRotationMotionPlanWithLift extends MotionPlanTestOpmode {

    @Override
    public MotionPlan getMotionPlan(Turret turret) {
        int testElevation = 300;
        int testRotation = 100;

        plan = new MotionPlan(turret);
        plan.add(new TurretPosition(0, 0, 0, 0));
        plan.add(new TurretPosition(0, testElevation, 0, 0));
        plan.add(new TurretPosition(testRotation, testElevation, 0, 0));
        plan.add(new TurretPosition(0, testElevation, 0, 0));
        plan.add(new TurretPosition(0, 0, 0, 0));
        return plan;
    }
}
