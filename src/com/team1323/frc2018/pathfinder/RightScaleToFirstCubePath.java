package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class RightScaleToFirstCubePath extends PathfinderPath{

	public RightScaleToFirstCubePath(){
		super.points = new Waypoint[]{
			new Waypoint(Constants.kRightScaleCorner.x() - Constants.ROBOT_HALF_LENGTH - 0.75, Constants.kRightScaleCorner.y() + Constants.ROBOT_HALF_WIDTH + 3.0, Pathfinder.d2r(180)),
			new Waypoint(Constants.kRightSwitchFarCorner.x() + 3.5, Constants.kRightSwitchFarCorner.y() + Constants.ROBOT_HALF_LENGTH + 1.75, Pathfinder.d2r(-90.0))
		};
		super.maxAccel = 5.0;
		super.defaultSpeed = 4.1;
		super.rotationScalar = 0.5;
		super.lookaheadPoints = 10;
	}
	
}
