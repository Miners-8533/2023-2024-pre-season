package org.csa.miners;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class TurretPositionReport {

    OpMode opMode;

    public TurretPositionReport(OpMode opMode) {
        this.opMode = opMode;
    }

    public void renderHeader() {
        this.opMode.telemetry.addData(">", "ROT   |EXT   |LIFT  |GRIP  ");
    }

    public void render(String caption, TurretPosition position) {
//        telemetry.addData(">", "GRIP |ELEV  |EXT  |ROT   ");
//        telemetry.addData(">", "GRIP |LLFT  |RLFT  |EXT   |ROT  ");
            this.opMode.telemetry.addData(caption, "|%05d |%05d |%05d |%4.2f ",
                    position.rotation(),
                    position.extension(),
                    position.elevation(),
                    position.grip()
            );
    }
}
