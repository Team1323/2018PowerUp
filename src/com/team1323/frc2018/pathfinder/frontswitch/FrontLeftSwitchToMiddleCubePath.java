package com.team1323.frc2018.pathfinder.frontswitch;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.pathfinder.PathfinderPath;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class FrontLeftSwitchToMiddleCubePath extends PathfinderPath{

	public FrontLeftSwitchToMiddleCubePath(){
		super.points = new Waypoint[]{
			new Waypoint(Constants.kLeftSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 1.0, Constants.kLeftSwitchCloseCorner.y() + Constants.ROBOT_HALF_WIDTH + 1.0, Pathfinder.d2r(-175)),
			new Waypoint(Constants.kLeftSwitchCloseCorner.x() - (2*Constants.kCubeWidth) - 1.75, Constants.kLeftSwitchCloseCorner.y() + Constants.ROBOT_HALF_WIDTH + 4.5, Pathfinder.d2r(90))	
		};
		super.maxAccel = 8.0;
		super.defaultSpeed = 4.9;
		super.rotationScalar = 1.5;
		super.lookaheadPoints = 10;
		super.usePID = true;
	}
	
}
