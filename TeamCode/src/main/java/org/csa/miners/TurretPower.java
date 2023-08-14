package org.csa.miners;

/**
 * Describe a power settings to apply to a {@link Turret}.
 *
 * Power is specified per axis (see {@link TurretPosition} and expects that the power will be tuned
 * in the turret itself to ensure that synchronized motors stay in sync.
 */
public class TurretPower {

    private double rotation;
    private double elevation;
    private double extension;
    private double grip;

    public TurretPower(double rotation, double elevation, double extension, double grip) {
        this.rotation = rotation;
        this.elevation = elevation;
        this.extension = extension;
        this.grip = grip;
    }

    public TurretPower(TurretPower other) {
        this.rotation = other.rotation;
        this.elevation = other.elevation;
        this.extension = other.extension;
        this.grip = other.grip;
    }

    public double getRotation() {
        return this.rotation;
    }

    public double getElevation() {
        return this.elevation;
    }

    public double getExtension() {
        return this.extension;
    }

    public double getGrip() {
        return this.grip;
    }

    public TurretPower offsetExtension(double offset) {
        TurretPower offsetPosition =  new TurretPower(this);
        offsetPosition.extension = offsetPosition.extension + offset;
        return offsetPosition;
    }

    public TurretPower offsetElevation(double offset) {
        TurretPower offsetPosition =  new TurretPower(this);
        offsetPosition.elevation = offsetPosition.elevation + offset;
        return offsetPosition;
    }


    public TurretPower offsetRotation(double offset) {
        TurretPower offsetPosition =  new TurretPower(this);
        offsetPosition.rotation = offsetPosition.rotation + offset;
        return offsetPosition;
    }

    public TurretPower setElevation(double elevation) {
        TurretPower offsetPosition =  new TurretPower(this);
        offsetPosition.elevation = elevation;
        return offsetPosition;
    }

    public TurretPower setExtension(double extension) {
        TurretPower offsetPosition =  new TurretPower(this);
        offsetPosition.extension = extension;
        return offsetPosition;
    }

    public TurretPower setRotation(double rotation) {
        TurretPower offsetPosition =  new TurretPower(this);
        offsetPosition.rotation = rotation;
        return offsetPosition;
    }

}
