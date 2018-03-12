package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class RightCubeToLeftScalePath extends PathfinderPath{
	
	public RightCubeToLeftScalePath(){
		super.points = new Waypoint[]{
			new Waypoint(Constants.kRightSwitchFarCorner.x() + 3.5, Constants.kRightSwitchFarCorner.y() + Constants.ROBOT_HALF_LENGTH - 0.75, Pathfinder.d2r(-45.0)),
			new Waypoint(21.0, 13.5, Pathfinder.d2r(-90)),
			new Waypoint(21.0, 8.5, Pathfinder.d2r(-90)),
			new Waypoint(Constants.kLeftScaleCorner.x() - Constants.ROBOT_HALF_LENGTH - 1.0, Constants.kLeftScaleCorner.y() - Constants.ROBOT_HALF_WIDTH - 0.5, Pathfinder.d2r(0))
		};
		super.maxAccel = 2.0;
		super.maxSpeed = 8.0;
		super.defaultSpeed = 5.6;
		super.lookaheadPoints = 10;
		super.rotationScalar = 0.8;
	}
	
}
