package com.team1323.frc2018.auto.modes;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.RobotState;
import com.team1323.frc2018.auto.AutoModeBase;
import com.team1323.frc2018.auto.AutoModeEndedException;
import com.team1323.frc2018.auto.actions.EjectCubeAction;
import com.team1323.frc2018.auto.actions.FollowPathAction;
import com.team1323.frc2018.auto.actions.ResetPoseAction;
import com.team1323.frc2018.auto.actions.SetTargetHeadingAction;
import com.team1323.frc2018.auto.actions.WaitAction;
import com.team1323.frc2018.auto.actions.WaitForElevatorAction;
import com.team1323.frc2018.auto.actions.WaitForHeadingAction;
import com.team1323.frc2018.auto.actions.WaitToFinishPathAction;
import com.team1323.frc2018.auto.actions.WaitToIntakeCubeAction;
import com.team1323.frc2018.auto.actions.WaitToPassXCoordinateAction;
import com.team1323.frc2018.pathfinder.PathManager;
import com.team1323.frc2018.subsystems.Elevator;
import com.team1323.frc2018.subsystems.Intake;
import com.team1323.frc2018.subsystems.Superstructure;
import com.team1323.frc2018.subsystems.Swerve;
import com.team254.lib.util.math.RigidTransform2d;
import com.team254.lib.util.math.Rotation2d;
import com.team254.lib.util.math.Translation2d;

import edu.wpi.first.wpilibj.Timer;

public class LeftSwitchLeftScaleMode extends AutoModeBase {

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
		//runAction(new SetTargetHeadingAction(150.0));
		Elevator.getInstance().setHighCurrentLimit();
		runAction(new WaitAction(0.25));
		Superstructure.getInstance().requestNonchalantIntakeConfig();
		runAction(new WaitToFinishPathAction());
		runAction(new WaitForElevatorAction());
		//RobotState.getInstance().resetRobotPosition(Constants.kLeftMostCubeCorner);
		//Superstructure.getInstance().requestOpenIntakingConfig();
		//Superstructure.getInstance().requestIntakingConfig();
		runAction(new FollowPathAction(PathManager.mLeftmostCubePickup, 135.0));
		runAction(new WaitToIntakeCubeAction(2.5));
		System.out.println("Intaken at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new FollowPathAction(PathManager.mLeftCubeToLeftScale, 45.0));
		//runAction(new WaitAction(0.75));
		//Swerve.getInstance().setAbsolutePathHeading(45.0);
		Superstructure.getInstance().requestConfig(35.0, Constants.ELEVATOR_BALANCED_SCALE_HEIGHT);
		runAction(new WaitToPassXCoordinateAction(23.0));
		runAction(new WaitForElevatorAction());
		runAction(new WaitForHeadingAction(35.0, 55.0));
		//runAction(new WaitToFinishPathAction());
		Intake.getInstance().strongEject();
		System.out.println("Second Cube scored at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new WaitAction(0.25));
		/*Swerve.getInstance().rotateInPlaceAbsolutely(135.0);
		runAction(new WaitAction(0.5));
		Superstructure.getInstance().requestIntakingConfig();
		runAction(new WaitForHeadingAction(125.0, 145.0));*/
		runAction(new FollowPathAction(PathManager.mLeftScaleToSecondCube, 135.0));
		runAction(new WaitAction(0.5));
		Superstructure.getInstance().requestIntakingConfig();
		//runAction(new WaitToPassXCoordinateAction(19.0));
		//RobotState.getInstance().resetRobotPosition(Constants.kSecondLeftCubeCorner);
		//Swerve.getInstance().enableCubeTracking(true);
		//Superstructure.getInstance().requestNonchalantIntakeConfig();
		runAction(new WaitToIntakeCubeAction(4.0));
		System.out.println("Third Cube intaken at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new FollowPathAction(PathManager.mSecondLeftCubeToScale, 45.0));
		Superstructure.getInstance().requestConfig(35.0, Constants.ELEVATOR_BALANCED_SCALE_HEIGHT);
		runAction(new WaitToPassXCoordinateAction(23.0));
		//runAction(new WaitToFinishPathAction());
		//Swerve.getInstance().rotateInPlaceAbsolutely(30.0);
		runAction(new WaitForHeadingAction(35.0, 55.0));
		runAction(new WaitForElevatorAction());
		Superstructure.getInstance().requestIntakeScore();
		System.out.println("Third Cube scored at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new WaitAction(0.25));
		runAction(new FollowPathAction(PathManager.mLeftScaleToThirdCube, 135.0));
		runAction(new WaitAction(0.5));
		Superstructure.getInstance().requestIntakingConfig();
		runAction(new WaitToIntakeCubeAction(4.0));
		System.out.println("Fourth Cube intaken at: " + (Timer.getFPGATimestamp() - startTime));
	}

}
