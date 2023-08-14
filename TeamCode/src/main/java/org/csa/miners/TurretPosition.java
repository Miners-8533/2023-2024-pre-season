package org.csa.miners;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.Math.abs;

/**
 * Describe a Pose for a {@link Turret}.
 *
 * Positions are defined using a RHR, such that encoders follow the following rules:
 * <ul>
 *     <li>Elevation Arm: Z-Axis</li>
 *     <li>Extension Arm: X-Axis</li
 *     <li>Rotation: Z-Axis Rotation</li>
 * </ul>
 */
public class TurretPosition {

    private int _rotation;
    private int _elevation;
    private int _extension;
    private double _grip;

    public TurretPosition(int rotation, int elevation, int extension, double grip) {
        this._rotation = rotation;
        this._elevation = elevation;
        this._extension = extension;
        this._grip = grip;
    }

    public TurretPosition(TurretPosition other) {
        this._rotation = other._rotation;
        this._elevation = other._elevation;
        this._extension = other._extension;
        this._grip = other._grip;
    }

    public int rotation() {
        return this._rotation;
    }

    public int elevation() {
        return this._elevation;
    }

    public int extension() {
        return this._extension;
    }

    public double grip() {
        return this._grip;
    }

    public TurretPosition opened(Turret turret) {
        TurretPosition opened =  new TurretPosition(this);
        opened._grip = turret.getGripperOpenPosition();
        return opened;
    }

    public TurretPosition closed(Turret turret) {
        TurretPosition closed =  new TurretPosition(this);
        closed._grip = turret.getGripperClosePosition();
        return closed;
    }

    public TurretPosition offsetExtension(int offset) {
        TurretPosition offsetPosition =  new TurretPosition(this);
        offsetPosition._extension = offsetPosition._extension + offset;
        return offsetPosition;
    }

    public TurretPosition offsetElevation(int offset) {
        TurretPosition offsetPosition =  new TurretPosition(this);
        offsetPosition._elevation = offsetPosition._elevation + offset;
        return offsetPosition;
    }


    public TurretPosition offsetRotation(int offset) {
        TurretPosition offsetPosition =  new TurretPosition(this);
        offsetPosition._rotation = offsetPosition._rotation + offset;
        return offsetPosition;
    }

    public TurretPosition setElevation(int elevation) {
        TurretPosition offsetPosition =  new TurretPosition(this);
        offsetPosition._elevation = elevation;
        return offsetPosition;
    }

    public TurretPosition setExtension(int extension) {
        TurretPosition offsetPosition =  new TurretPosition(this);
        offsetPosition._extension = extension;
        return offsetPosition;
    }

    public TurretPosition setRotation(int rotation) {
        TurretPosition offsetPosition =  new TurretPosition(this);
        offsetPosition._rotation = rotation;
        return offsetPosition;
    }



    public int getElevation() {
        return this._elevation;
    }

    public int getExtension() {
        return this._extension;
    }

    public int getRotation() {
        return this._rotation;
    }


    private static TurretPosition compositeDistance(TurretPosition left, TurretPosition right) {
        double rotationDistance = sqrt(pow(left.rotation(), 2) - pow(right.rotation(), 2));
        double elevationDistance = sqrt(pow(left.elevation(), 2) - pow(right.elevation(), 2));
        double extensionDistance = sqrt(pow(left.extension(), 2) - pow(right.extension(), 2));
        double gripDistance = sqrt(pow(left.grip(), 2) - pow(right.grip(), 2));
        return new TurretPosition((int)rotationDistance, (int)elevationDistance,
                (int)extensionDistance, gripDistance);
    }

    /**
     * Compute the Composite Absolute Distance between 2 points.
     *
     * Note: The order of left and right do not matter for this calculation.
     *
     * This is a much faster calculation than the
     * {@link TurretPosition#compositeDistance(TurretPosition, TurretPosition)}
     * method which relies on a lot of multiplication and division
     *
     * @param left The point to find the distance from
     * @param right The point to find the distance from
     * @return The absolute position between the points
     */
    private static TurretPosition absoluteDistanceComposite(TurretPosition left, TurretPosition right) {
        double rotationDistance = abs(left.rotation() - right.rotation());
        double elevationDistance = abs(left.elevation() - right.elevation());
        double extensionDistance = abs(left.extension() - right.extension());
        double gripDistance = abs(left.grip() - right.grip());
        return new TurretPosition((int)rotationDistance, (int)elevationDistance,
                (int)extensionDistance, gripDistance);
    }

    public TurretPosition absoluteDistanceComposite(TurretPosition other) {
        return TurretPosition.absoluteDistanceComposite(this, other);
    }

    /**
     * Determine if all elements of the {@link TurretPosition} are less than the elements of another
     * @param other The control position to test against
     * @return True if all elements are less than the elements of other
     */
    public boolean lessThan(TurretPosition other) {
        return this.extension() < other.extension() &&
                this.rotation() < other.extension() &&
                this.elevation() < other.elevation() &&
                this.grip() < other.grip();
    }

}
