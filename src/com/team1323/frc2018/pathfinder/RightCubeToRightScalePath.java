package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class RightCubeToRightScalePath extends PathfinderPath{

	public RightCubeToRightScalePath(){
		super.points = new Waypoint[]{
			//new Waypoint(Constants.kRightMostCube.x() + 1.85, Constants.kRightMostCube.y() + 1.85, Pathfinder.d2r(0)),
			//new Waypoint(Constants.kRightScaleCorner.x() - Constants.ROBOT_HALF_LENGTH - 1.5, Constants.kRightScaleCorner.y() + Constants.ROBOT_HALF_WIDTH + 1.0, Pathfinder.d2r(0))
			new Waypoint(Constants.kRightSwitchFarCorner.x() + 3.6, Constants.kRightSwitchFarCorner.y() + Constants.kRobotHalfLength - 0.5, Pathfinder.d2r(30.0)),
			new Waypoint(23.5, 22.0, Pathfinder.d2r(0.0))//23.5 22.5
		};
		super.maxSpeed = 8.0;
		super.maxAccel = 6.0;
		super.defaultSpeed = 4.1;
		super.rotationScalar = 1.0;
		super.lookaheadPoints = 20;
	}
	
}
