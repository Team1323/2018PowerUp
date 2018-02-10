package com.team1323.frc2018.auto.modes;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.auto.AutoModeBase;
import com.team1323.frc2018.auto.AutoModeEndedException;
import com.team1323.frc2018.auto.actions.EjectCubeAction;
import com.team1323.frc2018.auto.actions.FollowPathAction;
import com.team1323.frc2018.auto.actions.ResetPoseAction;
import com.team1323.frc2018.auto.actions.SetTargetHeadingAction;
import com.team1323.frc2018.auto.actions.WaitAction;
import com.team1323.frc2018.auto.actions.WaitToFinishPathAction;
import com.team1323.frc2018.auto.actions.WaitToIntakeCubeAction;
import com.team1323.frc2018.auto.actions.WaitToPassXCoordinateAction;
import com.team1323.frc2018.pathfinder.PathManager;
import com.team1323.frc2018.subsystems.Superstructure;
import com.team254.lib.util.math.RigidTransform2d;
import com.team254.lib.util.math.Rotation2d;
import com.team254.lib.util.math.Translation2d;

public class RightSwitchRightScaleMode extends AutoModeBase{
	@Override
	protected void routine() throws AutoModeEndedException {
		runAction(new ResetPoseAction(new RigidTransform2d(new Translation2d(Constants.ROBOT_HALF_LENGTH, Constants.kAutoStartingCorner.y() + Constants.ROBOT_HALF_WIDTH), Rotation2d.fromDegrees(0))));
		Superstructure.getInstance().requestIntakeHold();
		runAction(new FollowPathAction(PathManager.mRightSwitchDropoff, -90.0));
		Superstructure.getInstance().requestSwitchConfig();
		runAction(new WaitToPassXCoordinateAction(Constants.kRightSwitchCloseCorner.x()));
		runAction(new EjectCubeAction());
		runAction(new WaitToPassXCoordinateAction(Constants.kRightSwitchFarCorner.x()));
		runAction(new SetTargetHeadingAction(-180.0));
		runAction(new WaitAction(0.25));
		Superstructure.getInstance().requestIntakingConfig();
		runAction(new WaitToIntakeCubeAction());
		runAction(new FollowPathAction(PathManager.mRightCubeToRightScale, -360.0));
		Superstructure.getInstance().requestBalancedScaleConfig();
		runAction(new WaitToPassXCoordinateAction(23.2));
		Superstructure.getInstance().requestIntakeWeakScore();
		runAction(new WaitToFinishPathAction());
		//runAction(new FollowPathAction(PathManager.mRightScaleToSecondCube, -270.0));
		//runAction(new WaitToFinishPathAction());
	}

}
