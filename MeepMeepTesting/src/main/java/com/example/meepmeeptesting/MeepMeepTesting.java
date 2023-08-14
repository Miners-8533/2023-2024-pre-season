package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        // heading is based on Unit Circle convention, positive degrees = CCW, negative = CW.
        // default/'0' heading is positive x (or 'right' when looking from F5 tile start position)
        Pose2d startPose = new Pose2d(36, -54, Math.toRadians(90));
        Pose2d highTerm = new Pose2d(36, -12, Math.toRadians(135));
        Pose2d coneStack = new Pose2d(60, -12, Math.toRadians(0));
        // assuming red alliance, blue terminal (F5 tile start)
        Pose2d locationOne = new Pose2d(12, -12, Math.toRadians(180));

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(360), Math.toRadians(360), 11)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(startPose)
                                .lineToLinearHeading(new Pose2d(36,-12,0))
                                //.forward(42)
                                //.turn(Math.toRadians(-90))
                                .forward(24)
                                .waitSeconds(.5)
                                .lineToSplineHeading(highTerm)
                                .waitSeconds(.5)
                                .lineToSplineHeading(coneStack)
                                .lineToSplineHeading(highTerm)
                                .waitSeconds(.5)
                                .lineToSplineHeading(coneStack)
                                .lineToSplineHeading(highTerm)
                                .waitSeconds(.5)
                                .lineToSplineHeading(coneStack)
                                .lineToSplineHeading(highTerm)
                                .waitSeconds(.5)
                                .lineToSplineHeading(coneStack)
                                .lineToSplineHeading(highTerm)
                                .waitSeconds(.5)
                                .lineToSplineHeading(locationOne)
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}