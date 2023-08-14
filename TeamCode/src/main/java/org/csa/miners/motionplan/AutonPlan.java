package org.csa.miners.motionplan;

import org.csa.miners.Turret;
import org.csa.miners.TurretPosition;

/**
 * A MotionPlan for the PowerPlay Auton phase.
 * <p>The PowerPlay Auton phase begins with a cone pre-loaded on the robot.  The MotionPlan must
 * unload that cone onto a junction, then pick up 5 additional cones and place them on the same
 * junction</p>
 */
public class AutonPlan extends LoadUnloadPlan {

    /**
     *
     * @param turret The turret that will run the plan
     * @param load The Load point for the first cone in the auton stack
     * @param unload The Unload point for all the cones in the auton phase
     */
    public AutonPlan(Turret turret, TurretPosition load, TurretPosition unload) {
        super(turret);

        this.add(this.getLoadedHighLowTransitionHeight(load));
        this.add(this.getDropoffPlan(unload));
        this.add(this.getUnloadedHighLowTransitionHeight(load));

        int OFFSET = 30;

        for (int ii = 0; ii < 5; ii++) {
            this.add(this.buildCyclePlan(new TurretPosition(load.rotation(), load.elevation()+OFFSET*ii, load.extension(),load.grip()), unload));
        }

    }
}
