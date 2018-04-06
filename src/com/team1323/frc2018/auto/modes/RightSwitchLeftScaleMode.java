package com.team1323.frc2018.auto.modes;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.auto.AutoModeBase;
import com.team1323.frc2018.auto.AutoModeEndedException;
import com.team1323.frc2018.auto.actions.FollowPathAction;
import com.team1323.frc2018.auto.actions.ResetPoseAction;
import com.team1323.frc2018.auto.actions.WaitAction;
import com.team1323.frc2018.auto.actions.WaitForElevatorAction;
import com.team1323.frc2018.auto.actions.WaitForHeadingAction;
import com.team1323.frc2018.auto.actions.WaitForWallAction;
import com.team1323.frc2018.auto.actions.WaitToFinishPathAction;
import com.team1323.frc2018.auto.actions.WaitToIntakeCubeAction;
import com.team1323.frc2018.auto.actions.WaitToPassXCoordinateAction;
import com.team1323.frc2018.auto.actions.WaitToPassYCoordinateAction;
import com.team1323.frc2018.pathfinder.PathManager;
import com.team1323.frc2018.subsystems.Intake;
import com.team1323.frc2018.subsystems.Superstructure;
import com.team1323.frc2018.subsystems.Swerve;

import edu.wpi.first.wpilibj.Timer;

public class RightSwitchLeftScaleMode extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
		double startTime = Timer.getFPGATimestamp();
		runAction(new ResetPoseAction(Constants.kRobotStartingPose));
		Superstructure.getInstance().requestIntakeHold();
		runAction(new FollowPathAction(PathManager.mRightSwitchDropoff, 0.0));
		runAction(new WaitAction(0.5));
		Swerve.getInstance().setAbsolutePathHeading(-90.0);
		Superstructure.getInstance().requestSwitchConfig();
		runAction(new WaitToPassXCoordinateAction(Constants.kRightSwitchCloseCorner.x() - 1.0));
		Superstructure.getInstance().requestIntakeScore();
		runAction(new WaitToPassXCoordinateAction(Constants.kRightSwitchFarCorner.x()));
		runAction(new WaitAction(0.25));
		Superstructure.getInstance().requestNonchalantIntakeConfig();
		runAction(new WaitToFinishPathAction());
		runAction(new WaitForElevatorAction());
		runAction(new FollowPathAction(PathManager.mRightmostCubePickup, -170.0));
		Intake.getInstance().intakeWide();
		runAction(new WaitForWallAction(2.0));
		Intake.getInstance().intake();
		runAction(new WaitToIntakeCubeAction(0.75));
		System.out.println("Intaken at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new FollowPathAction(PathManager.mRightCubeToLeftScale, -180.0));
		runAction(new WaitAction(0.25));
		Superstructure.getInstance().requestPrimaryWristStow();
		runAction(new WaitToPassYCoordinateAction(17.5));
		Swerve.getInstance().setAbsolutePathHeading(-330.0);
		runAction(new WaitForHeadingAction(-340.0, -320.0));
		Superstructure.getInstance().requestConfig(35.0, Constants.kELevatorBalancedScaleHeight);
		runAction(new WaitToFinishPathAction());
		//runAction(new WaitToPassXCoordinateAction(22.75));
		runAction(new WaitForElevatorAction());
		Intake.getInstance().eject(Constants.kIntakeWeakEjectOutput);
		System.out.println("Second cube scored at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new WaitAction(0.25));
		runAction(new FollowPathAction(PathManager.mLeftScaleToFirstCube, -225.0));
		runAction(new WaitAction(0.75));
		/*Superstructure.getInstance().requestConfig(Constants.WRIST_INTAKING_ANGLE, Constants.ELEVATOR_INTAKING_HEIGHT);
		Intake.getInstance().open();
		runAction(new WaitToFinishPathAction());
		Intake.getInstance().intake();*/
		Superstructure.getInstance().requestNonchalantIntakeConfig();
		runAction(new WaitToIntakeCubeAction(3.0));
		System.out.println("Third cube intaken at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new FollowPathAction(PathManager.mLeftCubeToLeftScale, -330.0));
		runAction(new WaitAction(0.25));
		Superstructure.getInstance().requestConfig(35.0, Constants.kELevatorBalancedScaleHeight);
		runAction(new WaitToPassXCoordinateAction(22.75));
		runAction(new WaitForElevatorAction());
		runAction(new WaitForHeadingAction(-340.0 ,-320.0));
		Superstructure.getInstance().requestIntakeWeakScore();
		System.out.println("Third cube scored at: " + (Timer.getFPGATimestamp() - startTime));
	}

}
