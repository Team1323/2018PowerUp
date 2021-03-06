package com.team1323.frc2018.auto.modes;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.auto.AutoModeBase;
import com.team1323.frc2018.auto.AutoModeEndedException;
import com.team1323.frc2018.auto.actions.FollowPathAction;
import com.team1323.frc2018.auto.actions.ResetPoseAction;
import com.team1323.frc2018.auto.actions.WaitAction;
import com.team1323.frc2018.auto.actions.WaitToFinishPathAction;
import com.team1323.frc2018.auto.actions.WaitToPassXCoordinateAction;
import com.team1323.frc2018.pathfinder.PathManager;
import com.team1323.frc2018.subsystems.Superstructure;
import com.team1323.frc2018.subsystems.Swerve;
import com.team254.lib.util.math.RigidTransform2d;
import com.team254.lib.util.math.Rotation2d;
import com.team254.lib.util.math.Translation2d;

import edu.wpi.first.wpilibj.Timer;

public class TestMode extends AutoModeBase{

	@Override
	protected void routine() throws AutoModeEndedException {
		double startTime = Timer.getFPGATimestamp();
		runAction(new ResetPoseAction(new RigidTransform2d(new Translation2d(Constants.kRobotHalfLength, Constants.kAutoStartingCorner.y() + Constants.kRobotHalfWidth), Rotation2d.fromDegrees(0))));
		Superstructure.getInstance().requestIntakeHold();
		runAction(new FollowPathAction(PathManager.mTestPath, 0.0));
		runAction(new WaitAction(0.5));
		Swerve.getInstance().setAbsolutePathHeading(90.0);
		runAction(new WaitAction(0.5));
		Superstructure.getInstance().requestSwitchConfig();
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchCloseCorner.x()));
		Superstructure.getInstance().requestIntakeScore();
		runAction(new WaitToPassXCoordinateAction(Constants.kLeftSwitchFarCorner.x()));
		Swerve.getInstance().setAbsolutePathHeading(180.0);
		runAction(new WaitAction(0.25));
		Superstructure.getInstance().requestNonchalantIntakeConfig();
		runAction(new WaitToFinishPathAction());
	}

}
