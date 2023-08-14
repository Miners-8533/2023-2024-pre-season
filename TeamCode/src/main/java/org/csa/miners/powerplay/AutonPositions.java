package org.csa.miners.powerplay;

import org.csa.miners.TurretPosition;

public class AutonPositions {

    public static TurretPosition getTopLoadConeLeft() {
        return new TurretPosition(0, 180, 600, 0);
//        return new TurretPosition(0, 140, 600, 0);
    }

    public static TurretPosition getTopLoadConeRight() {
        return new TurretPosition(0, 180, 575, 0);
//        return new TurretPosition(0, 140, 600, 0);
    }

    public static TurretPosition getMiddleJunction() {
        return new TurretPosition(812, 680, 590, 0);
    }

    public static TurretPosition getLowJunction() {
        return new TurretPosition(376, 400, 223, 0);
    }

    public static TurretPosition getHighJunction() {
        return new TurretPosition(1135, 905, 575, 0);
//        return new TurretPosition(1115, 905, 585, 0);
//        return new TurretPosition(1115, 905, 595, 0);
//        return new TurretPosition(1110, 905, 600, 0);
//        return new TurretPosition(1100, 905, 610, 0);
//        return new TurretPosition(1140, 905, 624, 0);
    }
}
