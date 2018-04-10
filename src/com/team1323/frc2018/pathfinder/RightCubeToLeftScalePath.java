package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class RightCubeToLeftScalePath extends PathfinderPath{
	
	public RightCubeToLeftScalePath(){
		super.points = new Waypoint[]{
			new Waypoint(Constants.kRightSwitchFarCorner.x() + 3.5, Constants.kRightSwitchFarCorner.y() + Constants.kRobotHalfLength - 0.75, Pathfinder.d2r(-90.0)),
			new Waypoint(20.5, 17.0, Pathfinder.d2r(-90)),
			new Waypoint(20.5, 7.5, Pathfinder.d2r(-90)),
			new Waypoint(Constants.kLeftScaleCorner.x() - Constants.kRobotHalfLength - 1.0, Constants.kLeftScaleCorner.y() - Constants.kRobotHalfWidth - 1.5, Pathfinder.d2r(0))
		};
		super.maxAccel = 2.0;
		super.maxSpeed = 8.0;
		super.defaultSpeed = 5.6;
		super.lookaheadPoints = 10;
		super.rotationScalar = 0.8;
	}
	
}
