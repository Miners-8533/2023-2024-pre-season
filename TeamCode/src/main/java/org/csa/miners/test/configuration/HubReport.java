package org.csa.miners.test.configuration;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareDevice;

@Disabled
@TeleOp(name="Hub Report")
public class HubReport extends LinearOpMode {

    Boolean showClass = true;
    Boolean showSerialization = false;

    @Override
    public void runOpMode() throws InterruptedException {


//        LynxModule hub = hardwareMap.tryGet(LynxModule.class, "USB DQ2ES2WH");
//        String devName = "USB DQ2ES2WH (HW: 20, Maj: 1, Min: 8, Eng: 2)";
        String devName = "Expansion Hub (HW: 20, Maj: 1, Min: 8, Eng: 2)";
        LynxModule hub = hardwareMap.tryGet(LynxModule.class, devName);

        try {
            telemetry.addData("Hub SN", hub.getDeviceName());
        } catch (NullPointerException ex) {
            telemetry.addLine("Couldn't find the Hub");
        }

        for (HardwareDevice dev : hardwareMap) {
            if (dev.getDeviceName().contains("Expansion Hub")) {
                if (dev.getDeviceName().contains(devName)) {
                    telemetry.addLine("");
                    telemetry.addLine("----- FOUND IT -----");
                    telemetry.addLine("");
                    hub = (LynxModule) dev;

                    telemetry.addData("Hub SN", hub.getSerialNumber());
                    telemetry.addData("", hub.getDeviceName())
                    ;

                }
                telemetry.addLine(dev.getDeviceName());
                telemetry.addData("  >", dev.getConnectionInfo());
                telemetry.addData("  >", dev.getManufacturer().toString());
                telemetry.addData("  >", Integer.toString(dev.getVersion()));

                if (showClass) {
                    telemetry.addData("  >", dev.getClass().getName());
                }

                if (showSerialization) {
                    telemetry.addData("  >", dev.toString());
                }
            }

        }

        telemetry.update();
        waitForStart();
    }
}
