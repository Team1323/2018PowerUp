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
import com.team1323.frc2018.auto.actions.WaitToPassYCoordinateAction;
import com.team1323.frc2018.pathfinder.PathManager;
import com.team1323.frc2018.subsystems.Intake;
import com.team1323.frc2018.subsystems.Superstructure;
import com.team1323.frc2018.subsystems.Swerve;
import com.team254.lib.util.math.RigidTransform2d;
import com.team254.lib.util.math.Rotation2d;
import com.team254.lib.util.math.Translation2d;

import edu.wpi.first.wpilibj.Timer;

public class LeftSwitchRightScaleMode extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
		double startTime = Timer.getFPGATimestamp();
		runAction(new ResetPoseAction(Constants.kRobotStartingPose));
		Superstructure.getInstance().requestIntakeHold();
		runAction(new FollowPathAction(PathManager.mLeftSwitchDropoff, 0.0));
		runAction(new WaitAction(0.5));
		Swerve.getInstance().setAbsolutePathHeading(90.0);
		runAction(new WaitAction(0.5));
		Superstructure.getInstance().requestSwitchConfig();
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x()));
		Superstructure.getInstance().requestIntakeScore();
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchFarCorner.x()));
		runAction(new WaitAction(0.25));
		Superstructure.getInstance().requestConfig(Constants.kWristIntakingAngle, Constants.kElevatorIntakingHeight);
		runAction(new WaitToFinishPathAction());
		runAction(new WaitForElevatorAction());
		runAction(new FollowPathAction(PathManager.mLeftmostCubePickup, 160.0));
		Intake.getInstance().intakeWide();
		runAction(new WaitForWallAction(2.0));
		Intake.getInstance().intake();
		runAction(new WaitToIntakeCubeAction(0.75));
		System.out.println("Intaken at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new FollowPathAction(PathManager.mLeftCubeToRightScale, 180.0));
		runAction(new WaitAction(0.25));
		Superstructure.getInstance().requestPrimaryWristStow();
		runAction(new WaitToPassYCoordinateAction(10.0));
		Swerve.getInstance().setAbsolutePathHeading(330.0);
		runAction(new WaitForHeadingAction(320.0, 340.0));
		Superstructure.getInstance().requestConfig(35.0, Constants.kELevatorBalancedScaleHeight);
		//runAction(new WaitToPassXCoordinateAction(22.75));
		runAction(new WaitToFinishPathAction());
		runAction(new WaitForElevatorAction());
		Intake.getInstance().eject(Constants.kIntakeWeakEjectOutput);
		System.out.println("Second cube scored at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new WaitAction(0.25));
		runAction(new FollowPathAction(PathManager.mRightScaleToFirstCube, 180.0));
		runAction(new WaitAction(0.5));
		Superstructure.getInstance().requestConfig(Constants.kWristIntakingAngle, Constants.kElevatorIntakingHeight);
		Intake.getInstance().open();
		runAction(new WaitToFinishPathAction());
		Intake.getInstance().intake();
		runAction(new WaitToIntakeCubeAction(0.5));
		if(!Intake.getInstance().hasCube()){
			runAction(new DriveStraightAction(Rotation2d.fromDegrees(180).toTranslation().scale(0.35)));
			runAction(new WaitToIntakeCubeAction(2.0));
		}
		System.out.println("Third cube intaken at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new ResetPoseAction(new RigidTransform2d(new Translation2d(Constants.kRightSwitchFarCorner.x() + 3.5, Constants.kRightSwitchFarCorner.y() + Constants.kRobotHalfLength - 0.75), Rotation2d.fromDegrees(Swerve.getInstance().getPose().getRotation().getUnboundedDegrees()))));
		runAction(new FollowPathAction(PathManager.mRightCubeToRightScale, 315.0));
		Superstructure.getInstance().requestConfig(35.0, Constants.kELevatorBalancedScaleHeight);
		//runAction(new WaitToPassXCoordinateAction(22.75));
		runAction(new WaitForElevatorAction());
		runAction(new WaitForHeadingAction(305.0, 325.0));
		Intake.getInstance().eject(Constants.kIntakeWeakEjectOutput);
		System.out.println("Third cube scored at: " + (Timer.getFPGATimestamp() - startTime));
	}

}
