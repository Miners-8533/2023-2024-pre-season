package org.csa.miners.motionplan;

import org.csa.miners.Turret;
import org.csa.miners.TurretPosition;

/**
 * An {@link ApproachElement} is similar to a {@link MotionElement} that only needs to get
 * <i>close</i> to its target instead of needing to hit it's target.
 *
 * Note: {@link ApproachElement} will continue to target its position if no other plan elements
 * override the targetted position
 */
public class ApproachElement implements MotionPlanElement {

    TurretPosition position;
    Turret turret;
    TurretPosition error;

    /**
     * Create a new {@link ApproachElement}.
     * @param turret The turret to apply the motion to
     * @param position The position to approach
     * @param error The allowed error in the positioning
     */
    public ApproachElement(Turret turret, TurretPosition position, TurretPosition error) {
        this.position = position;
        this.turret = turret;
        this.error = error;
    }

    /**
     * Create a new {@link ApproachElement}.
     * @param turret The turret to apply the motion to
     * @param position The position to approach
     * @param error The allowed error in the positioning to apply to all axes
     */
    public ApproachElement(Turret turret, TurretPosition position, int error) {
        this.position = position;
        this.turret = turret;
        this.position = new TurretPosition(error, error, error, error);
    }

    @Override
    public void initiateMotion(MotionPlan plan) {
        this.turret.moveTo(this.position, plan.defaultPlanPower);
        this.waitForMotionToStart();
    }

    @Override
    public boolean isComplete() {
        TurretPosition distance = this.turret.getCurrentPosition().absoluteDistanceComposite(this.position);
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

}
