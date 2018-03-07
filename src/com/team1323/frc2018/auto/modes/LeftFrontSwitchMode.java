package com.team1323.frc2018.auto.modes;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.RobotState;
import com.team1323.frc2018.auto.AutoModeBase;
import com.team1323.frc2018.auto.AutoModeEndedException;
import com.team1323.frc2018.auto.actions.FollowPathAction;
import com.team1323.frc2018.auto.actions.ResetPoseAction;
import com.team1323.frc2018.auto.actions.WaitAction;
import com.team1323.frc2018.auto.actions.WaitForHeadingAction;
import com.team1323.frc2018.auto.actions.WaitToIntakeCubeAction;
import com.team1323.frc2018.auto.actions.WaitToPassXCoordinateAction;
import com.team1323.frc2018.pathfinder.PathManager;
import com.team1323.frc2018.subsystems.Superstructure;
import com.team254.lib.util.math.RigidTransform2d;
import com.team254.lib.util.math.Rotation2d;
import com.team254.lib.util.math.Translation2d;

import edu.wpi.first.wpilibj.Timer;

public class LeftFrontSwitchMode extends AutoModeBase{

	@Override
	protected void routine() throws AutoModeEndedException {
		double startTime = Timer.getFPGATimestamp();
		runAction(new ResetPoseAction(new RigidTransform2d(new Translation2d(Constants.ROBOT_HALF_LENGTH, Constants.kAutoStartingCorner.y() + Constants.ROBOT_HALF_WIDTH), Rotation2d.fromDegrees(0))));
		Superstructure.getInstance().requestIntakeHold();
		runAction(new FollowPathAction(PathManager.mFrontLeftSwitchPath, 0.0));
		Superstructure.getInstance().requestSwitchConfig();
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 0.75));
		Superstructure.getInstance().requestIntakeScore();
		System.out.println("First Cube Scored at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new WaitAction(0.25));
		runAction(new FollowPathAction(PathManager.mFrontLeftSwitchToOuterCube, 45.0));
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 1.0));
		Superstructure.getInstance().requestNonchalantIntakeConfig();
		runAction(new WaitToIntakeCubeAction(3.0));
		System.out.println("Second Cube Intaken at: " + (Timer.getFPGATimestamp() - startTime));
		//RobotState.getInstance().resetRobotPosition(Constants.kRightSwitchTarget);
		runAction(new FollowPathAction(PathManager.mOuterCubeToFrontLeftSwitch, 0.0));
		Superstructure.getInstance().requestSwitchConfig();
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 0.75));
		Superstructure.getInstance().requestIntakeScore();
		System.out.println("Second Cube Scored at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new WaitAction(0.25));
		runAction(new FollowPathAction(PathManager.mFrontLeftSwitchToMiddleCube, 45.0));
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 1.0));
		Superstructure.getInstance().requestHighIntakingConfig();
		runAction(new WaitToIntakeCubeAction(3.0));
		System.out.println("Third Cube Intaken at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new FollowPathAction(PathManager.mMiddleCubeToFrontLeftSwitch, 0.0));
		Superstructure.getInstance().requestSwitchConfig();
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 0.75));
		Superstructure.getInstance().requestIntakeScore();
		System.out.println("Third Cube Scored at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new WaitAction(0.25));
		runAction(new FollowPathAction(PathManager.mFrontLeftSwitchToMiddleCube, 45.0));
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 1.0));
		Superstructure.getInstance().requestNonchalantIntakeConfig();
		runAction(new WaitToIntakeCubeAction(3.0));
		System.out.println("Fourth Cube Intaken at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new FollowPathAction(PathManager.mMiddleCubeToFrontLeftSwitch, 0.0));
		Superstructure.getInstance().requestSwitchConfig();
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 0.75));
		Superstructure.getInstance().requestIntakeScore();
		System.out.println("Fourth Cube Scored at: " + (Timer.getFPGATimestamp() - startTime));
	}
	
}
