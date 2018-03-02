package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class RightmostCubePickupPath extends PathfinderPath{
	
	public RightmostCubePickupPath(){
		super.points = new Waypoint[]{
			//new Waypoint(Constants.kRightMostCube.x() + (1.4*Math.sqrt(3.0)), Constants.kRightMostCube.y() + 1.4, Pathfinder.d2r(-150.0)),//1.65
			//new Waypoint(Constants.kRightMostCube.x(), Constants.kRightMostCube.y(), Pathfinder.d2r(-150.0))
			new Waypoint(Constants.kRightSwitchFarCorner.x() + 3.5, Constants.kRightSwitchFarCorner.y() + Constants.ROBOT_HALF_LENGTH + 1.0, Pathfinder.d2r(-90.0)),
			new Waypoint(Constants.kRightSwitchFarCorner.x() + 3.5, Constants.kRightSwitchFarCorner.y() + Constants.ROBOT_HALF_LENGTH - 0.75, Pathfinder.d2r(-90.0))
		};
		super.defaultSpeed = 4.0;
		super.maxSpeed = 4.0;
		super.maxAccel = 10.0;
		super.rotationScalar = 1.9;
		super.lookaheadPoints = 20;
	}
	
}
