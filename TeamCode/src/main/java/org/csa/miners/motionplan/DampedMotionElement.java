package org.csa.miners.motionplan;

import org.csa.miners.Turret;
import org.csa.miners.TurretPosition;

public class DampedMotionElement implements MotionPlanElement {


    TurretPosition target;
    Turret turret;
    TurretPosition error;

    /**
     * Create a new {@link DampedMotionElement}.
     * @param turret The turret to apply the motion to
     * @param targetPosition The position to target
     * @param error The allowed error in the positioning
     */
    public DampedMotionElement(Turret turret, TurretPosition targetPosition, TurretPosition error) {
        this.target = targetPosition;
        this.turret = turret;
        this.error = error;
    }

    @Override
    public void initiateMotion(MotionPlan plan) {
        this.turret.moveTo(this.target, plan.defaultPlanPower);
        this.waitForMotionToStart();
    }

    @Override
    public boolean isComplete() {
        TurretPosition distance = this.turret.getCurrentPosition().absoluteDistanceComposite(this.target);
        return distance.lessThan(this.error);
    }

    private void waitForMotionToStart() {
        this.sleep(100);
    }

    /**
     * Sleeps for the given amount of milliseconds, or until the thread is interrupted. This is
     * simple shorthand for the operating-system-provided {@link Thread#sleep(long) sleep()} method.
     *
     * @param milliseconds amount of time to sleep, in milliseconds
     * @see Thread#sleep(long)
     */
    public final void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void accept(MotionPlanElementReportVisitor visitor) {
        visitor.visit(this);
    }
}
