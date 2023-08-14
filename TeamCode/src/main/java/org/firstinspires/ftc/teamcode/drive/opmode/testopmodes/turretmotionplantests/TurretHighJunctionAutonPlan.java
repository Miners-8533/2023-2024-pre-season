package org.firstinspires.ftc.teamcode.drive.opmode.testopmodes.turretmotionplantests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.csa.miners.Turret;
import org.csa.miners.motionplan.AutonMotionPlans;
import org.csa.miners.motionplan.MotionPlan;
import org.csa.miners.powerplay.AutonPositions;

/**
 * OpMode tests the saved position for the MidHeight Junction by delivering the preloaded cone
 */

@Disabled
@Autonomous(name="Motion Plan - High Junction Auton", group="MotionPlan Tests")
public class TurretHighJunctionAutonPlan extends MotionPlanTestOpmode {

    public MotionPlan getMotionPlan(Turret turret) {
        return AutonMotionPlans.getJunctionPlan(turret, AutonPositions.getHighJunction());
    }
}
