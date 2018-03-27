package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class LeftCubeToRightScalePath extends PathfinderPath{
	public LeftCubeToRightScalePath(){
		super.points = new Waypoint[]{
			new Waypoint(Constants.kLeftSwitchFarCorner.x() + 3.6, Constants.kLeftSwitchFarCorner.y() + 0.4, Pathfinder.d2r(45)),
			new Waypoint(21.0, 10.0, Pathfinder.d2r(90)),
			new Waypoint(21.0, 21.0, Pathfinder.d2r(90)),
			new Waypoint(Constants.kRightScaleCorner.x() - Constants.kRobotHalfLength - 0.75, Constants.kRightScaleCorner.y() + Constants.kRobotHalfWidth + 3.0, Pathfinder.d2r(0))
		};
		super.maxAccel = 2.0;
		super.maxSpeed = 8.0;
		super.defaultSpeed = 5.6;
		super.lookaheadPoints = 10;
		super.rotationScalar = 0.7;
	}
}
