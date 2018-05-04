package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class StartToRightScalePath extends PathfinderPath{

	public StartToRightScalePath(){
		super.points = new Waypoint[]{
			new Waypoint(Constants.kRobotLeftStartingPose.getTranslation().x(), Constants.kRobotLeftStartingPose.getTranslation().y(), Pathfinder.d2r(0)),
			new Waypoint(Constants.kLeftSwitchFarCorner.x(), Constants.kLeftSwitchFarCorner.y() - Constants.kRobotHalfLength - 1.25, Pathfinder.d2r(0)),
			new Waypoint(20.35, 10.0, Pathfinder.d2r(90)),
			new Waypoint(20.35, 21.0, Pathfinder.d2r(90)),
			new Waypoint(Constants.kRightScaleCorner.x() - Constants.kRobotHalfLength - 0.75, Constants.kRightScaleCorner.y() + Constants.kRobotHalfWidth + 2.5, Pathfinder.d2r(0))
		};
		super.maxAccel = 2.0;
		super.maxSpeed = 8.0;//8
		super.lookaheadPoints = 10;
		super.defaultSpeed = 7.0;//7
		super.rotationScalar = 0.75;
	}
	
}
