package com.team1323.frc2018.auto.modes;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.auto.AutoModeBase;
import com.team1323.frc2018.auto.AutoModeEndedException;
import com.team1323.frc2018.auto.actions.DriveStraightAction;
import com.team1323.frc2018.auto.actions.FollowPathAction;
import com.team1323.frc2018.auto.actions.ResetPoseAction;
import com.team1323.frc2018.auto.actions.WaitAction;
import com.team1323.frc2018.auto.actions.WaitForElevatorAction;
import com.team1323.frc2018.auto.actions.WaitForWallAction;
import com.team1323.frc2018.auto.actions.WaitToFinishPathAction;
import com.team1323.frc2018.auto.actions.WaitToIntakeCubeAction;
import com.team1323.frc2018.auto.actions.WaitToPassXCoordinateAction;
import com.team1323.frc2018.auto.actions.WaitToPassYCoordinateAction;
import com.team1323.frc2018.pathfinder.PathManager;
import com.team1323.frc2018.subsystems.Intake;
import com.team1323.frc2018.subsystems.Superstructure;
import com.team1323.frc2018.subsystems.Swerve;
import com.team254.lib.util.math.Rotation2d;

import edu.wpi.first.wpilibj.Timer;

public class RightScaleMode extends AutoModeBase{

	@Override
	protected void routine() throws AutoModeEndedException {
		double startTime = Timer.getFPGATimestamp();
		runAction(new ResetPoseAction(Constants.kRobotLeftStartingPose));
		Superstructure.getInstance().requestIntakeHold();
		Superstructure.getInstance().requestConfig(Constants.kWristPrimaryStowAngle);
		runAction(new FollowPathAction(PathManager.mStartToRightScale, -90.0));
		//runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchFarCorner.x()));
		runAction(new WaitToPassYCoordinateAction(15.0));
		//Swerve.getInstance().setAbsolutePathHeading(-30.0);
		Superstructure.getInstance().requestConfig(35.0, Constants.kELevatorBalancedScaleHeight);
		runAction(new WaitToPassYCoordinateAction(17.0));
		Swerve.getInstance().setAbsolutePathHeading(-40.0);
		runAction(new WaitToFinishPathAction());
		//runAction(new WaitToPassXCoordinateAction(21.0));
		runAction(new WaitForElevatorAction());
		Intake.getInstance().eject(Constants.kIntakeEjectOutput);
		System.out.println("First cube scored at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new WaitAction(0.25));
		runAction(new FollowPathAction(PathManager.mRightScaleToFirstCube, -180.0));
		runAction(new WaitAction(0.5));
		Superstructure.getInstance().requestConfig(Constants.kWristIntakingAngle, Constants.kElevatorIntakingHeight);
		Intake.getInstance().intakeWide();
		runAction(new WaitForWallAction(2.5));
		Intake.getInstance().intake();
		runAction(new WaitToIntakeCubeAction(0.75));
		if(!Intake.getInstance().hasCube()){
			runAction(new DriveStraightAction(Rotation2d.fromDegrees(180).toTranslation().scale(0.35)));
			runAction(new WaitToIntakeCubeAction(2.0));
			/*if(Intake.getInstance().hasCube())
				Swerve.getInstance().setXCoordinate(Constants.kRightSwitchFarCorner.x() + 3.5);*/
		}
		System.out.println("Second cube intaken at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new FollowPathAction(PathManager.mAlternateRightCubeToRightScale, -55.0));//-45
		runAction(new WaitAction(0.25));
		Superstructure.getInstance().requestConfig(35.0, Constants.kELevatorBalancedScaleHeight);
		runAction(new WaitToFinishPathAction());
		runAction(new WaitForElevatorAction());
		Intake.getInstance().eject(Constants.kIntakeEjectOutput);
		System.out.println("Second cube scored at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new WaitAction(0.25));
		runAction(new FollowPathAction(PathManager.mAlternateRightScaleToSecondCube, -135.0));
		runAction(new WaitAction(0.75));
		Superstructure.getInstance().requestNonchalantIntakeConfig();
		runAction(new WaitToIntakeCubeAction(3.5));
		if(!Intake.getInstance().hasCube()){
			Superstructure.getInstance().requestIntakingConfig();
			runAction(new DriveStraightAction(Rotation2d.fromDegrees(-135.0).toTranslation().scale(0.35)));
			runAction(new WaitToIntakeCubeAction(1.5));
		}
		System.out.println("Third Cube intaken at: " + (Timer.getFPGATimestamp() - startTime));
	}

}
