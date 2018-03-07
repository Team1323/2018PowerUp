package com.team1323.frc2018.pathfinder.frontswitch;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.pathfinder.PathfinderPath;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class MiddleCubeToFrontLeftSwitchPath extends PathfinderPath{

	public MiddleCubeToFrontLeftSwitchPath(){
		super.points = new Waypoint[]{
			new Waypoint(Constants.kLeftSwitchCloseCorner.x() - (2*Constants.kCubeWidth) - 1.25, Constants.kLeftSwitchCloseCorner.y() + Constants.ROBOT_HALF_WIDTH + 3.5, Pathfinder.d2r(-90)),
			new Waypoint(Constants.kLeftSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 1.0, Constants.kLeftSwitchCloseCorner.y() + Constants.ROBOT_HALF_WIDTH + 1.0, Pathfinder.d2r(0))
		};
		super.maxAccel = 8.0;
		super.defaultSpeed = 4.9;
		super.rotationScalar = 1.0;
		super.lookaheadPoints = 20;
	}
	
}
