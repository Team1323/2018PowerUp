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
		runAction(new ResetPoseAction(Constants.kRobotStartingPose));
		Superstructure.getInstance().requestIntakeHold();
		runAction(new FollowPathAction(PathManager.mStartToLeftScale, 0.0));
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x()));
		Superstructure.getInstance().requestConfig(66.0, 3.5);
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchFarCorner.x()));
		Swerve.getInstance().setAbsolutePathHeading(45.0);
		//runAction(new WaitToPassXCoordinateAction(22.5));
		runAction(new WaitToFinishPathAction());
		Intake.getInstance().strongEject();
		System.out.println("First Cube Scored at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new WaitAction(0.5));
		/*runAction(new FollowPathAction(PathManager.mLeftScaleToFirstCube, 135.0));
		runAction(new WaitAction(0.25));
		Superstructure.getInstance().requestNonchalantIntakeConfig();
		runAction(new WaitToIntakeCubeAction(3.0));*/
		runAction(new FollowPathAction(PathManager.mLeftmostCubePickup, 190.0));
		runAction(new WaitForHeadingAction(60.0, 180.0));
		Superstructure.getInstance().requestConfig(Constants.WRIST_INTAKING_ANGLE, Constants.ELEVATOR_INTAKING_HEIGHT);
		Intake.getInstance().intakeWide();
		runAction(new WaitToFinishPathAction());
		Intake.getInstance().intake();
		runAction(new WaitToIntakeCubeAction(1.0));
		if(Intake.getInstance().getAverageCurrent() < 20.0 && !Intake.getInstance().hasCube()){
			System.out.println("Drive straight action initiated");
			runAction(new DriveStraightAction(Rotation2d.fromDegrees(180.0).toTranslation().scale(0.5)));
			runAction(new WaitToIntakeCubeAction(1.5));
		}
		System.out.println("Second cube intaken at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new FollowPathAction(PathManager.mDerpLeftCubeToLeftScale, 45.0));
		runAction(new WaitAction(0.25));
		Superstructure.getInstance().requestConfig(35.0, Constants.ELEVATOR_HIGH_SCALE_HEIGHT);
		//runAction(new WaitToPassXCoordinateAction(23.0));
		runAction(new WaitToFinishPathAction());
		runAction(new WaitForElevatorAction());
		//runAction(new WaitForHeadingAction(35.0, 55.0));
		Intake.getInstance().strongEject();
		System.out.println("Second Cube scored at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new WaitAction(0.25));
		runAction(new FollowPathAction(PathManager.mLeftScaleToSecondCube, 150.0));
		//runAction(new WaitForHeadingAction(60.0, 180.0));
		runAction(new WaitAction(0.5));
		Superstructure.getInstance().requestNonchalantIntakeConfig();
		runAction(new WaitToIntakeCubeAction(4.0));
		System.out.println("Third Cube intaken at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new FollowPathAction(PathManager.mSecondLeftCubeToScale, 45.0));
		Superstructure.getInstance().requestConfig(35.0, Constants.ELEVATOR_HIGH_SCALE_HEIGHT);
		runAction(new WaitToPassXCoordinateAction(23.0));
		runAction(new WaitForHeadingAction(35.0, 55.0));
		runAction(new WaitForElevatorAction());
		Superstructure.getInstance().requestIntakeScore();
		System.out.println("Third Cube scored at: " + (Timer.getFPGATimestamp() - startTime));
	}
	
}
