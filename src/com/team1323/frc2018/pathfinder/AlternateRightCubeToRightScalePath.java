package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class AlternateRightCubeToRightScalePath extends PathfinderPath{

	public AlternateRightCubeToRightScalePath(){
		super.points = new Waypoint[]{
			//new Waypoint(Constants.kRightMostCube.x() + 1.85, Constants.kRightMostCube.y() + 1.85, Pathfinder.d2r(0)),
			//new Waypoint(Constants.kRightScaleCorner.x() - Constants.ROBOT_HALF_LENGTH - 1.5, Constants.kRightScaleCorner.y() + Constants.ROBOT_HALF_WIDTH + 1.0, Pathfinder.d2r(0))
			new Waypoint(Constants.kRightSwitchFarCorner.x() + 3.5, Constants.kRightSwitchFarCorner.y() + Constants.kRobotHalfLength -0.5, Pathfinder.d2r(30.0)),//3.5 1.75
			new Waypoint(Constants.kRightScaleCorner.x() - Constants.kRobotHalfLength - 0.25, Constants.kRightScaleCorner.y() + Constants.kRobotHalfWidth + 2.5, Pathfinder.d2r(0))
		};
		super.maxSpeed = 8.0;
		super.maxAccel = 6.0;
		super.defaultSpeed = 4.1;
		super.rotationScalar = 1.25;
		super.lookaheadPoints = 20;
	}
	
}
