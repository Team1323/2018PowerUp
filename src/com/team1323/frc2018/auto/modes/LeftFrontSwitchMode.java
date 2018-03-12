package com.team1323.frc2018.auto.modes;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.RobotState;
import com.team1323.frc2018.auto.AutoModeBase;
import com.team1323.frc2018.auto.AutoModeEndedException;
import com.team1323.frc2018.auto.actions.FollowPathAction;
import com.team1323.frc2018.auto.actions.ResetPoseAction;
import com.team1323.frc2018.auto.actions.WaitAction;
import com.team1323.frc2018.auto.actions.WaitForElevatorAction;
import com.team1323.frc2018.auto.actions.WaitToIntakeCubeAction;
import com.team1323.frc2018.auto.actions.WaitToPassXCoordinateAction;
import com.team1323.frc2018.pathfinder.PathManager;
import com.team1323.frc2018.subsystems.Superstructure;
import com.team1323.frc2018.subsystems.Swerve;

import edu.wpi.first.wpilibj.Timer;

public class LeftFrontSwitchMode extends AutoModeBase{

	@Override
	protected void routine() throws AutoModeEndedException {
		double startTime = Timer.getFPGATimestamp();
		runAction(new ResetPoseAction(Constants.kRobotStartingPose));
		Superstructure.getInstance().requestIntakeHold();
		runAction(new FollowPathAction(PathManager.mFrontLeftSwitchPath, 0.0));
		runAction(new WaitAction(0.5));
		Superstructure.getInstance().requestGroundStowedConfig();
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 1.4));
		Superstructure.getInstance().requestIntakeScore();
		System.out.println("First Cube Scored at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new WaitAction(0.5));		
		runAction(new FollowPathAction(PathManager.mFrontLeftSwitchToOuterCube, 30.0));
		//runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 1.0));
		Superstructure.getInstance().requestNonchalantIntakeConfig();
		runAction(new WaitToIntakeCubeAction(3.0));
		System.out.println("Second Cube Intaken at: " + (Timer.getFPGATimestamp() - startTime));
		RobotState.getInstance().resetRobotPosition(Constants.kRightSwitchTarget);		
		runAction(new FollowPathAction(PathManager.mOuterCubeToFrontLeftSwitch, 0.0));
		Superstructure.getInstance().requestPrimaryWristStow();
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 1.25));
		Superstructure.getInstance().requestIntakeScore();
		System.out.println("Second Cube Scored at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new WaitAction(0.5));
		runAction(new FollowPathAction(PathManager.mFrontLeftSwitchToMiddleCube, 20.0));
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 1.0));
		Superstructure.getInstance().requestHighIntakingConfig();
		runAction(new WaitToIntakeCubeAction(3.0));
		System.out.println("Third Cube Intaken at: " + (Timer.getFPGATimestamp() - startTime));
		//RobotState.getInstance().resetRobotPosition(Constants.kRightSwitchTarget);
		runAction(new FollowPathAction(PathManager.mMiddleCubeToFrontLeftSwitch, 0.0));
		Superstructure.getInstance().requestSwitchConfig();
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 1.25));
		Superstructure.getInstance().requestIntakeScore();
		System.out.println("Third Cube Scored at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new WaitAction(0.5));
		runAction(new FollowPathAction(PathManager.mFrontLeftSwitchToMiddleCube, 30.0));
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 0.5));
		Superstructure.getInstance().requestNonchalantIntakeConfig();
		//runAction(new WaitForElevatorAction());
		//RobotState.getInstance().resetRobotPosition(Constants.kLeftSwitchTarget);
		//Swerve.getInstance().setAbsolutePathHeading(30.0);
		runAction(new WaitToIntakeCubeAction(3.0));
		System.out.println("Fourth Cube Intaken at: " + (Timer.getFPGATimestamp() - startTime));
		//RobotState.getInstance().resetRobotPosition(Constants.kRightSwitchTarget);
		runAction(new FollowPathAction(PathManager.mMiddleCubeToFrontLeftSwitch, 0.0));
		Superstructure.getInstance().requestGroundStowedConfig();
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 1.25));
		Superstructure.getInstance().requestIntakeScore();
		System.out.println("Fourth Cube Scored at: " + (Timer.getFPGATimestamp() - startTime));
	}
	
}
