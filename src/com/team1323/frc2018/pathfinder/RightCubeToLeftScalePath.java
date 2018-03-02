package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class RightCubeToLeftScalePath extends PathfinderPath{
	
	public RightCubeToLeftScalePath(){
		super.points = new Waypoint[]{
			new Waypoint(Constants.kRightMostCube.x() + 1.85, Constants.kRightMostCube.y() + 1.85, Pathfinder.d2r(-90)),
			new Waypoint(21.0, 13.5, Pathfinder.d2r(-90)),
			new Waypoint(21.0, 10.0, Pathfinder.d2r(-90)),
			new Waypoint(Constants.kLeftScaleCorner.x() - Constants.ROBOT_HALF_LENGTH - 1.0, Constants.kLeftScaleCorner.y() - Constants.ROBOT_HALF_WIDTH, Pathfinder.d2r(0))
		};
		super.maxAccel = 3.0;
		super.maxSpeed = 8.0;
		super.defaultSpeed = 6.9;
	}
	
}
