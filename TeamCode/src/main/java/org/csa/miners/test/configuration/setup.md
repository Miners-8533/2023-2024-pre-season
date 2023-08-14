# Setup
This package depends on the idea that applying positive power causes encoders to have increasing
values.  The requirement that encoders count up when positive power is applied comes from the core FTC
libraries we use are using.  Specifically, the RUN_TO_POSITION mode expects encoders to count up
when positive power is applied.

We've set up some test Autom functions to help you if your robot is going to wierd
locations, having unpredictable motions, or if speeds do not seem to adjust with changes to power
levels.


## Test Extension

To test the Extension Motor, we will start with the TestExtensionMotorDirection Auton to ensure that
the encoders are counting up when applying positive power.  It's a good idea to position the
extension arm halfway between retracted and extended so that it doesn't hit dead stops during this
test.  After running the Auton, the final position will be shown onscreen and so will an indication
of if the motor rotated positive or negative.

If rotation doesn't run in the correct direction, try switching the polarity of the motor.  We also
found reference to special encoder wire harnesses, but haven't tested them.
NOTE: Changing the motor direction from FORWARD to REVERSE does not resolve this
issue.  Changing Motor direction changes both the direction and the encoder value.

Once you have your motor encoder increasing with positive power, you can test that the
RUN_TO_POSITION mode functions correctly using the TestExtensionRunToPosition Auton.  This should
extend the extension arm smoothly to encoder position 100.  If the extension arm tries to retract
instead of extend, you need to change the DcMotor.Direction in your motor initialization.

## Test Rotation

To test the rotational motors, start with the TestRotationMotorDirection class to ensure that the
encoders are counting up when applying positive power.  If rotation doesn't run in the correct
direction, try switching the polarity of the motor.  We also found reference to special encoder wire
harnesses, but haven't tested them.
NOTE: Changing the motor direction from FORWARD to REVERSE does not resolve this
issue.  Changing Motor direction changes both the direction and the encoder value.

Once you have your motor encoder increasing with positive power, you can test that the
RUN_TO_POSITION mode functions correctly using the TestRotationRunToPosition class.  This should
rotate the turret smoothly to encoder position 100.

Finally, you can use TurretRotationMotionPlan to ensure that Turret Motion Planning moves the turret
to according to the Turret's Right Hand Coordinate System.  WARNING: The TurretRotationMotionPlan
rotation may collide with your chassis.  Make sure to elevate the turret so that any components
will not impact during rotation.  If you've already tested the lift, you can use
TurretLiftRotationMotionPlan for your testing instead of TurretRotationMotionPlan.
The TurretRotationMotionPlan will rotate the Turret to index 100.  Since this is a positive
location, the turret should rotate counter clockwise according to the Right Hand Rule.  If the
rotation is in the wrong direction, change the DcMotor.Direction and rerun the test.
