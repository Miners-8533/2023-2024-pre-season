package org.csa.miners.motionplan;

import org.csa.miners.Turret;

public interface MotionPlanElement {
    public void initiateMotion(MotionPlan plan);
    public boolean isComplete();
}
