package org.csa.miners.motionplan;

import org.csa.miners.Turret;

public class WaitForCloseElement implements MotionPlanElement {

    Turret turret;

    public WaitForCloseElement(Turret turret) {
        this.turret = turret;
    }

    @Override
    public void initiateMotion(MotionPlan plan) {
    }

    @Override
    public boolean isComplete() {
        return this.turret.isGripperClosed();
    }

}
