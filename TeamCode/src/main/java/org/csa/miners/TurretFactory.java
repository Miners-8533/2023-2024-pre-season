package org.csa.miners;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoController;

import java.util.ArrayList;
import java.util.List;

public class TurretFactory {

    public static String getTestRigID() {
        return "DQ2ES2WH";
    }

    public static Turret getTurretFromHardwareMap(OpMode opMode, HardwareMap hardwareMap) {

        if (TurretFactory.isRunningOnHubID(hardwareMap, TurretFactory.getTestRigID())) {
            opMode.telemetry.addLine("Running on Test Rig");
            return TestRigTurret.TestRigTurretFromMap(hardwareMap);
        } else {
            opMode.telemetry.addLine("Running on Robot");
            return AutonTurret.AutonTurretFromMap(hardwareMap);
        }
    }
    public static Turret getTurretFromHardwareMap(HardwareMap hardwareMap) {
        Turret turret;

        if (TurretFactory.isRunningOnHubID(hardwareMap, TurretFactory.getTestRigID())) {
            return TestRigTurret.TestRigTurretFromMap(hardwareMap);
        } else {
            return AutonTurret.AutonTurretFromMap(hardwareMap);
        }
    }

    public static Boolean isRunningOnTestRig(HardwareMap hardwareMap) {
        return TurretFactory.isRunningOnHubID(hardwareMap, TurretFactory.getTestRigID());
    }

    private static Boolean isRunningOnHubID(HardwareMap hardwareMap, String id) {

        for (LynxModule hub : TurretFactory.getExpansionHubs(hardwareMap)) {
            if (hub.getSerialNumber().equals(id)) {
                return true;
            }
        }

        return false;
    }

    private static List<LynxModule> getExpansionHubs(HardwareMap hardwareMap) {

        List<LynxModule> result = new ArrayList<>();

        for (HardwareDevice dev : hardwareMap) {
            if (LynxModule.class.isInstance(dev)) {
                result.add((LynxModule) dev);
            }
        }
        return result;
    }
}
