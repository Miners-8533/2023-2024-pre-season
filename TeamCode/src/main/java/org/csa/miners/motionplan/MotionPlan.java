package org.csa.miners.motionplan;

import org.csa.miners.Turret;
import org.csa.miners.TurretPosition;
import org.csa.miners.TurretPower;

import java.util.ArrayList;

public class MotionPlan {

    ArrayList<MotionPlanElement> _plan;
    Turret _turret;
    TurretPower defaultPlanPower;

    private int currentStep = 0;
    private boolean planInProgress = false;
    private boolean planComplete = false;

    public MotionPlan(Turret turret) {
        this._plan = new ArrayList<>();
        this._turret = turret;
        this.defaultPlanPower = turret.getDefaultPower();
    }

    public MotionPlan(Turret turret, TurretPower defaultPower) {
        this._plan = new ArrayList<>();
        this._turret = turret;
        this.defaultPlanPower = defaultPower;
    }

    public int getCurrentStepIndex() {
        return currentStep;
    }

    public int getTotalSteps() {
        return this._plan.size();
    }

    /**
     * Add a {@link MotionElement} to the plan by defining the target position.
     *
     * @param position The {@link TurretPosition} to move to.
     */
    public void add(TurretPosition position) {
        this._plan.add(new MotionElement(this._turret, position));
    }

    /**
     * Add a {@link ApproachElement} to the plan by defining the target position and delta.
     *
     * @param position The {@link TurretPosition} to move to.
     */
    public void add(TurretPosition position, TurretPosition delta) {
        this._plan.add(new ApproachElement(this._turret, position, delta));
    }

    /**
     * Add a generic {@link MotionPlanElement} to the plan.
     *
     * @param element
     */
    public void add(MotionPlanElement element) {
        this._plan.add(element);
    }

    /**
     * Add another {@link MotionPlan} to the end of this plan.
     *
     * @param plan The other plan to append to this plan.
     */
    public void add(MotionPlan plan) {
        this._plan.addAll(plan._plan);
    }

    public boolean isPlanInProgress() {
        return planInProgress;
    }

    public boolean isPlanComplete() {
        return planComplete;
    }

    private boolean isOnLastStep() {
        return this.currentStep == this._plan.size()-1;
    }

    private boolean isNotOnLastStep() {
        return !this.isOnLastStep();
    }

    public void start() {
        this.planInProgress = true;
        this.getCurrentStep().initiateMotion(this);
    }

    private MotionPlanElement getCurrentStep() {
        return this._plan.get(this.currentStep);
    }

    public void advance() {

        if (this.getCurrentStep().isComplete() && this.isNotOnLastStep()) {
            this.currentStep++;
            this.getCurrentStep().initiateMotion(this);

        } else if (this.getCurrentStep().isComplete() && this.isOnLastStep()) {
            this.planComplete = true;

        }


//        if ((!this._turret.isBusy() || this.currentStep == 0)
//                && this.currentStep < this._plan.size()) {
//            this.getCurrentStep().initiateMotion(this);
//            this.currentStep++;
//
//        } else if (!this._turret.isBusy()
//                && this.currentStep >= this._plan.size()) {
//
//            this.planComplete = true;
////            this.planInProgress = false;
//
//        }
    }

    public void resetPlan() {
        this.currentStep = 0;
        this.planComplete = false;
        this.planInProgress = false;
    }

}
