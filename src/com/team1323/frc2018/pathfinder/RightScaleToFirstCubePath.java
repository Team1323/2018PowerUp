package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class RightScaleToFirstCubePath extends PathfinderPath{

	public RightScaleToFirstCubePath(){
		super.points = new Waypoint[]{
			new Waypoint(Constants.kRightScaleCorner.x() - Constants.kRobotHalfLength - 0.75, Constants.kRightScaleCorner.y() + Constants.kRobotHalfWidth + 3.0, Pathfinder.d2r(180)),
			new Waypoint(Constants.kRightSwitchFarCorner.x() + 4.5, Constants.kRightSwitchFarCorner.y() + Constants.kRobotHalfLength + 0.0, Pathfinder.d2r(-135.0))//3.5 1.75
		};
		super.maxAccel = 5.0;
		super.defaultSpeed = 3.1;
		super.rotationScalar = 1.0;
		super.lookaheadPoints = 10;
	}
	
}
