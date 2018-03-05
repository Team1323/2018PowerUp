package com.team1323.frc2018.auto.modes;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.auto.AutoModeBase;
import com.team1323.frc2018.auto.AutoModeEndedException;
import com.team1323.frc2018.auto.actions.FollowPathAction;
import com.team1323.frc2018.auto.actions.ResetPoseAction;
import com.team1323.frc2018.auto.actions.WaitAction;
import com.team1323.frc2018.auto.actions.WaitForElevatorAction;
import com.team1323.frc2018.auto.actions.WaitForHeadingAction;
import com.team1323.frc2018.auto.actions.WaitToFinishPathAction;
import com.team1323.frc2018.auto.actions.WaitToIntakeCubeAction;
import com.team1323.frc2018.auto.actions.WaitToPassXCoordinateAction;
import com.team1323.frc2018.auto.actions.WaitToPassYCoordinateAction;
import com.team1323.frc2018.pathfinder.PathManager;
import com.team1323.frc2018.subsystems.Elevator;
import com.team1323.frc2018.subsystems.Intake;
import com.team1323.frc2018.subsystems.Superstructure;
import com.team1323.frc2018.subsystems.Swerve;
import com.team254.lib.util.math.RigidTransform2d;
import com.team254.lib.util.math.Rotation2d;
import com.team254.lib.util.math.Translation2d;

import edu.wpi.first.wpilibj.Timer;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class LeftSwitchRightScaleMode extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
		double startTime = Timer.getFPGATimestamp();
		runAction(new ResetPoseAction(new RigidTransform2d(new Translation2d(Constants.ROBOT_HALF_LENGTH, Constants.kAutoStartingCorner.y() + Constants.ROBOT_HALF_WIDTH), Rotation2d.fromDegrees(0))));
		Superstructure.getInstance().requestIntakeHold();
		runAction(new FollowPathAction(PathManager.mLeftSwitchDropoff, 0.0));
		Elevator.getInstance().setLowCurrentLimit();
		runAction(new WaitAction(0.5));
		Swerve.getInstance().setAbsolutePathHeading(90.0);
		runAction(new WaitAction(0.5));
		Superstructure.getInstance().requestSwitchConfig();
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x()));
		Superstructure.getInstance().requestIntakeScore();
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchFarCorner.x()));
		Elevator.getInstance().setHighCurrentLimit();
		runAction(new WaitAction(0.25));
		Superstructure.getInstance().requestNonchalantIntakeConfig();
		runAction(new WaitToFinishPathAction());
		runAction(new WaitForElevatorAction());
		runAction(new FollowPathAction(PathManager.mLeftmostCubePickup, 135.0));
		runAction(new WaitToIntakeCubeAction(2.5));
		System.out.println("Intaken at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new FollowPathAction(PathManager.mLeftCubeToRightScale, 180.0));
		runAction(new WaitAction(0.25));
		Superstructure.getInstance().requestPrimaryWristStow();
		runAction(new WaitToPassYCoordinateAction(10.0));
		Swerve.getInstance().setAbsolutePathHeading(330.0);
		runAction(new WaitForHeadingAction(320.0, 340.0));
		Superstructure.getInstance().requestConfig(35.0, Constants.ELEVATOR_BALANCED_SCALE_HEIGHT);
		runAction(new WaitToPassXCoordinateAction(22.75));
		runAction(new WaitForElevatorAction());
		Intake.getInstance().strongEject();
		System.out.println("Second cube scored at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new WaitAction(0.25));
		runAction(new FollowPathAction(PathManager.mRightScaleToFirstCube, 225.0));
		runAction(new WaitAction(0.25));
		Superstructure.getInstance().requestNonchalantIntakeConfig();
		runAction(new WaitToIntakeCubeAction(3.0));
		System.out.println("Third cube intaken at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new ResetPoseAction(new RigidTransform2d(new Translation2d(Constants.kRightSwitchFarCorner.x() + 3.5, Constants.kRightSwitchFarCorner.y() + Constants.ROBOT_HALF_LENGTH - 0.75), Rotation2d.fromDegrees(Swerve.getInstance().getPose().getRotation().getUnboundedDegrees()))));
		runAction(new FollowPathAction(PathManager.mRightCubeToRightScale, 315.0));
		Superstructure.getInstance().requestConfig(35.0, Constants.ELEVATOR_BALANCED_SCALE_HEIGHT);
		runAction(new WaitToPassXCoordinateAction(22.75));
		runAction(new WaitForElevatorAction());
		runAction(new WaitForHeadingAction(305 ,325.0));
		Superstructure.getInstance().requestIntakeScore();
		System.out.println("Third cube scored at: " + (Timer.getFPGATimestamp() - startTime));
	}

}
