package com.team1323.frc2018.auto.modes;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.auto.AutoModeBase;
import com.team1323.frc2018.auto.AutoModeEndedException;
import com.team1323.frc2018.auto.actions.DriveStraightAction;
import com.team1323.frc2018.auto.actions.FollowPathAction;
import com.team1323.frc2018.auto.actions.ResetPoseAction;
import com.team1323.frc2018.auto.actions.WaitAction;
import com.team1323.frc2018.auto.actions.WaitForElevatorAction;
import com.team1323.frc2018.auto.actions.WaitToFinishPathAction;
import com.team1323.frc2018.auto.actions.WaitToIntakeCubeAction;
import com.team1323.frc2018.auto.actions.WaitToPassXCoordinateAction;
import com.team1323.frc2018.pathfinder.PathManager;
import com.team1323.frc2018.subsystems.Intake;
import com.team1323.frc2018.subsystems.Superstructure;
import com.team254.lib.util.math.RigidTransform2d;
import com.team254.lib.util.math.Rotation2d;
import com.team254.lib.util.math.Translation2d;

import edu.wpi.first.wpilibj.Timer;

public class RightFrontSwitchMode extends AutoModeBase{
	boolean leak = false;
	
	public RightFrontSwitchMode(boolean leak){
		this.leak = leak;
	}
	
	@Override
	protected void routine() throws AutoModeEndedException {
		double startTime = Timer.getFPGATimestamp();
		runAction(new ResetPoseAction(Constants.kRobotStartingPose.transformBy(RigidTransform2d.fromTranslation(new Translation2d(-0.25, 0.0)))));
		Superstructure.getInstance().requestIntakeHold();
		runAction(new FollowPathAction(PathManager.mFrontRightSwitch, 0.0));
		runAction(new WaitAction(0.5));
		Superstructure.getInstance().requestConfig(75.0, Constants.kElevatorSecondCubeHeight);
		runAction(new WaitToFinishPathAction());
		Superstructure.getInstance().requestIntakeScore();
		System.out.println("First Cube Scored at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new WaitAction(0.25));
		runAction(new FollowPathAction(PathManager.mFrontRightSwitchToOuterCube, 0.0));
		runAction(new WaitAction(0.35));
		Superstructure.getInstance().requestConfig(Constants.kWristIntakingAngle, Constants.kElevatorIntakingHeight);
		Intake.getInstance().intakeWide();
		runAction(new WaitToFinishPathAction(3.5));
		Intake.getInstance().intake();
		runAction(new WaitToIntakeCubeAction(1.0));
		if(!Intake.getInstance().hasCube()){
			System.out.println("Moving forward extra");
			Intake.getInstance().intake();
			runAction(new DriveStraightAction(Rotation2d.fromDegrees(0).toTranslation().scale(0.3)));
			runAction(new WaitToIntakeCubeAction(1.0));
		}
		System.out.println("Second Cube Intaken at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new FollowPathAction(PathManager.mOuterCubeToFrontRightSwitch, 0.0));
		runAction(new WaitAction(0.5));
		Superstructure.getInstance().requestConfig(75.0, Constants.kElevatorSecondCubeHeight);
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x() - Constants.kRobotHalfLength - 0.75));
		Superstructure.getInstance().requestIntakeScore();
		System.out.println("Second Cube Scored at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new WaitAction(0.25));
		runAction(new FollowPathAction(PathManager.mFrontRightSwitchToMiddleCube, 0.0));//-30
		runAction(new WaitAction(0.25));
		Superstructure.getInstance().requestConfig(Constants.kWristIntakingAngle, Constants.kElevatorSecondCubeHeight);
		Intake.getInstance().intakeWide();
		runAction(new WaitToFinishPathAction(3.5));
		Intake.getInstance().intake();
		runAction(new WaitToIntakeCubeAction(1.0));
		if(!Intake.getInstance().hasCube()){
			System.out.println("Moving forward extra");
			Intake.getInstance().intake();
			runAction(new DriveStraightAction(Rotation2d.fromDegrees(0).toTranslation().scale(0.3)));
			runAction(new WaitToIntakeCubeAction(1.0));
		}
		System.out.println("Third Cube Intaken at: " + (Timer.getFPGATimestamp() - startTime));
		runAction(new FollowPathAction(PathManager.mMiddleCubeToFrontRightSwitch, 0.0));
		runAction(new WaitAction(0.25));
		Superstructure.getInstance().requestSwitchConfig();
		runAction(new WaitToFinishPathAction());
		Superstructure.getInstance().requestIntakeWeakScore();
		System.out.println("Third Cube Scored at: " + (Timer.getFPGATimestamp() - startTime));
		if(leak){
			runAction(new WaitAction(0.75));
			runAction(new FollowPathAction(PathManager.mFrontRightSwitchToDropoff, 0.0));
			Superstructure.getInstance().requestConfig(Constants.kWristPrimaryStowAngle);
			runAction(new WaitForElevatorAction());
			Superstructure.getInstance().requestGroundStowedConfig();
			runAction(new WaitToFinishPathAction());
		}else{
			runAction(new WaitAction(0.75));
			runAction(new FollowPathAction(PathManager.mFrontRightSwitchToBottomMiddle, -30.0));
			runAction(new WaitAction(0.75));
			Superstructure.getInstance().requestNonchalantIntakeConfig();
			runAction(new WaitToFinishPathAction(3.0));
			runAction(new WaitToIntakeCubeAction(1.0));
			System.out.println("Fourth Cube Intaken at: " + (Timer.getFPGATimestamp() - startTime));
		}
		
		

		/*runAction(new FollowPathAction(PathManager.mMiddleCubeToFrontRightSwitch, 0.0));
		Superstructure.getInstance().requestGroundStowedConfig();
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x() - Constants.kRobotHalfLength - 1.25));
		Superstructure.getInstance().requestIntakeScore();
		System.out.println("Fourth Cube Scored at: " + (Timer.getFPGATimestamp() - startTime));*/
	}

}
