package org.csa.miners.motionplan;

/**
 * Reporting Object that visits a MotionPlanElement
 */
public interface MotionPlanElementReportVisitor {
    public void visit(ApproachElement element);
    public void visit(DampedMotionElement element);
    public void visit(MotionElement element);
    public void visit(ReplayElement element);
    public void visit(WaitElement element);
    public void visit(WaitForCloseElement element);
}
