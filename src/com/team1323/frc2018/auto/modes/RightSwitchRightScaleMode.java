package com.team1323.frc2018.auto.modes;

import com.team1323.frc2018.auto.AutoModeBase;
import com.team1323.frc2018.auto.AutoModeEndedException;
import com.team1323.frc2018.auto.actions.EjectCubeAction;
import com.team1323.frc2018.auto.actions.FollowPathAction;
import com.team1323.frc2018.auto.actions.SetTargetHeadingAction;
import com.team1323.frc2018.auto.actions.WaitToFinishPathAction;
import com.team1323.frc2018.auto.actions.WaitToPassXCoordinateAction;
import com.team1323.frc2018.pathfinder.PathManager;

public class RightSwitchRightScaleMode extends AutoModeBase{

	@Override
	protected void routine() throws AutoModeEndedException {
		runAction(new FollowPathAction(PathManager.mRightSwitchDropoff, -90.0));
		runAction(new WaitToPassXCoordinateAction(10.0));
		runAction(new EjectCubeAction());
		runAction(new WaitToPassXCoordinateAction(16.25));
		runAction(new SetTargetHeadingAction(180.0));
		runAction(new WaitToFinishPathAction());
	}

}
