package com.team1323.frc2018.auto.modes;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.auto.AutoModeBase;
import com.team1323.frc2018.auto.AutoModeEndedException;
import com.team1323.frc2018.auto.actions.DriveStraightAction;
import com.team1323.frc2018.auto.actions.FollowPathAction;
import com.team1323.frc2018.auto.actions.ResetPoseAction;
import com.team1323.frc2018.auto.actions.WaitAction;
import com.team1323.frc2018.auto.actions.WaitForElevatorAction;
import com.team1323.frc2018.auto.actions.WaitForHeadingAction;
import com.team1323.frc2018.auto.actions.WaitForWallAction;
import com.team1323.frc2018.auto.actions.WaitToFinishPathAction;
import com.team1323.frc2018.auto.actions.WaitToIntakeCubeAction;
import com.team1323.frc2018.auto.actions.WaitToPassXCoordinateAction;
import com.team1323.frc2018.pathfinder.PathManager;
import com.team1323.frc2018.subsystems.Intake;
import com.team1323.frc2018.subsystems.Superstructure;
import com.team1323.frc2018.subsystems.Swerve;
import com.team254.lib.util.math.Rotation2d;

import edu.wpi.first.wpilibj.Timer;

public class LeftScaleMode extends AutoModeBase{

	@Override
	protected void routine() throws AutoModeEndedException {
		double startTime = Timer.getFPGATimestamp();
		runAction(new ResetPoseAction(Constants.kRobotLeftStartingPose));
		Superstructure.getInstance().requestIntakeHold();
		runAction(new FollowPathAction(PathManager.mStartToLeftScale, 50.0));
		//runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x() - 5.0));
		//Swerve.getInstance().setAbsolutePathHeading(50.0);
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x()));
		Superstructure.getInstance().requestConfig(66.0, 4.5);//66 3.75
		runAction(new WaitToFinishPathAction());
		Intake.getInstance().eject(Constants.kIntakeStrongEjectOutput);
		System.out.println("First Cube Scored at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new WaitAction(0.4));
		runAction(new FollowPathAction(PathManager.mAlternateLeftmostCube, 170.0));
		runAction(new WaitForHeadingAction(80.0, 180.0));
		Superstructure.getInstance().requestConfig(Constants.kWristIntakingAngle, Constants.kElevatorIntakingHeight);
		Intake.getInstance().intakeWide();
		runAction(new WaitForWallAction(3.0));
		Intake.getInstance().intake();
		runAction(new WaitToIntakeCubeAction(1.0));
		System.out.println("Switch X: " + Swerve.getInstance().getPose().getTranslation().x());
		if(!Intake.getInstance().hasCube()){
			System.out.println("Drive straight action initiated");
			runAction(new DriveStraightAction(Rotation2d.fromDegrees(180.0).toTranslation().scale(0.3)));
			runAction(new WaitToIntakeCubeAction(1.5));
			if(Intake.getInstance().hasCube())
				Swerve.getInstance().setXCoordinate(19.0);//19.7
		}
		System.out.println("Second cube intaken at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new FollowPathAction(PathManager.mDerpLeftCubeToLeftScale, 35.0));//40
		runAction(new WaitAction(0.25));
		Superstructure.getInstance().requestConfig(60.0, Constants.kELevatorBalancedScaleHeight);
		//runAction(new WaitToPassXCoordinateAction(23.0));
		runAction(new WaitToFinishPathAction());
		runAction(new WaitForElevatorAction());
		//runAction(new WaitForHeadingAction(35.0, 55.0));
		Intake.getInstance().eject(Constants.kIntakeEjectOutput);
		System.out.println("Second Cube scored at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new WaitAction(0.25));
		runAction(new FollowPathAction(PathManager.mAlternateLeftScaleToSecondCube, 150.0));
		//runAction(new WaitForHeadingAction(60.0, 180.0));
		runAction(new WaitAction(0.75));
		Superstructure.getInstance().requestConfig(Constants.kWristIntakingAngle, Constants.kElevatorIntakingHeight);
		Intake.getInstance().intakeWide();
		runAction(new WaitToFinishPathAction());
		Intake.getInstance().intake();
		runAction(new WaitToIntakeCubeAction(0.75));
		if(!Intake.getInstance().hasCube()){
			System.out.println("Drive straight action initiated");
			Intake.getInstance().intake();
			runAction(new DriveStraightAction(Rotation2d.fromDegrees(120.0).toTranslation().scale(0.25)));
			runAction(new WaitToIntakeCubeAction(1.5));
		}
		System.out.println("Third Cube intaken at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new FollowPathAction(PathManager.mAlternateSecondLeftCubeToScale, 55.0));
		runAction(new WaitAction(0.25));
		Superstructure.getInstance().requestConfig(60.0, Constants.kELevatorBalancedScaleHeight);
		runAction(new WaitToFinishPathAction());
		runAction(new WaitForElevatorAction());
		Superstructure.getInstance().requestIntakeScore();
		System.out.println("Third Cube scored at: " + (Timer.getFPGATimestamp() - startTime));
	}
	
}
