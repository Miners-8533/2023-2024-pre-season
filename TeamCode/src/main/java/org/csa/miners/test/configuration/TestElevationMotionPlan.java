package org.csa.miners.test.configuration;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.csa.miners.motionplan.MotionPlan;
import org.csa.miners.Turret;
import org.csa.miners.TurretPosition;
import org.firstinspires.ftc.teamcode.drive.opmode.testopmodes.turretmotionplantests.MotionPlanTestOpmode;

@Disabled
@Autonomous(name="MotionPlan - Elevation", group="MotionPlan Tests")
public class TestElevationMotionPlan extends MotionPlanTestOpmode {

    @Override
    public MotionPlan getMotionPlan(Turret turret) {
        plan = new MotionPlan(turret);
        plan.add(new TurretPosition(0, 0, 0, 0));
        plan.add(new TurretPosition(0, 200, 0, 0));
        plan.add(new TurretPosition(0, 0, 0, 0));
        return plan;
    }
}
