package com.team1323.frc2018.auto.modes;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.RobotState;
import com.team1323.frc2018.auto.AutoModeBase;
import com.team1323.frc2018.auto.AutoModeEndedException;
import com.team1323.frc2018.auto.actions.FollowPathAction;
import com.team1323.frc2018.auto.actions.ResetPoseAction;
import com.team1323.frc2018.auto.actions.WaitAction;
import com.team1323.frc2018.auto.actions.WaitToFinishPathAction;
import com.team1323.frc2018.auto.actions.WaitToIntakeCubeAction;
import com.team1323.frc2018.auto.actions.WaitToPassXCoordinateAction;
import com.team1323.frc2018.pathfinder.PathManager;
import com.team1323.frc2018.subsystems.Intake;
import com.team1323.frc2018.subsystems.Superstructure;

import edu.wpi.first.wpilibj.Timer;

public class LeftFrontSwitchMode extends AutoModeBase{

	@Override
	protected void routine() throws AutoModeEndedException {
		double startTime = Timer.getFPGATimestamp();
		runAction(new ResetPoseAction(Constants.kRobotStartingPose));
		Superstructure.getInstance().requestIntakeHold();
		runAction(new FollowPathAction(PathManager.mFrontLeftSwitch, 0.0));
		runAction(new WaitAction(0.5));
		//Superstructure.getInstance().requestGroundStowedConfig();
		Superstructure.getInstance().requestConfig(85.0, Constants.ELEVATOR_SECOND_CUBE_HEIGHT);
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 1.75));
		Superstructure.getInstance().requestIntakeScore();
		System.out.println("First Cube Scored at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new WaitAction(0.0));
		runAction(new FollowPathAction(PathManager.mFrontLeftSwitchToOuterCube, 0.0));
		runAction(new WaitAction(0.25));
		//runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 1.0));
		Superstructure.getInstance().requestConfig(Constants.WRIST_INTAKING_ANGLE, Constants.ELEVATOR_INTAKING_HEIGHT);
		Intake.getInstance().intakeWide();
		runAction(new WaitToFinishPathAction());
		Intake.getInstance().intake();
		runAction(new WaitToIntakeCubeAction(3.0));
		System.out.println("Second Cube Intaken at: " + (Timer.getFPGATimestamp() - startTime));
		//RobotState.getInstance().resetRobotPosition(Constants.kRightSwitchTarget);
		runAction(new FollowPathAction(PathManager.mOuterCubeToFrontLeftSwitch, 0.0));
		runAction(new WaitAction(0.5));
		Superstructure.getInstance().requestPrimaryWristStow();
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 1.0));
		Superstructure.getInstance().requestIntakeScore();
		System.out.println("Second Cube Scored at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new WaitAction(0.25));
		runAction(new FollowPathAction(PathManager.mFrontLeftSwitchToMiddleCube, 0.0));//35
		//runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 0.75));
		runAction(new WaitAction(0.25));
		//Superstructure.getInstance().requestHighIntakingConfig();
		Superstructure.getInstance().requestConfig(Constants.WRIST_INTAKING_ANGLE, Constants.ELEVATOR_SECOND_CUBE_HEIGHT);
		Intake.getInstance().open();
		runAction(new WaitToFinishPathAction());
		Intake.getInstance().intake();
		runAction(new WaitToIntakeCubeAction(3.0));
		System.out.println("Third Cube Intaken at: " + (Timer.getFPGATimestamp() - startTime));
		//RobotState.getInstance().resetRobotPosition(Constants.kRightSwitchTarget);
		runAction(new FollowPathAction(PathManager.mMiddleCubeToFrontLeftSwitch, 0.0));
		Superstructure.getInstance().requestSwitchConfig();
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 1.0));
		Superstructure.getInstance().requestIntakeWeakScore();
		System.out.println("Third Cube Scored at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new WaitAction(0.5));
		runAction(new FollowPathAction(PathManager.mFrontLeftSwitchToBottomMiddle, 30.0));
		//runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 0.5));
		runAction(new WaitAction(0.5));
		Superstructure.getInstance().requestNonchalantIntakeConfig();
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
