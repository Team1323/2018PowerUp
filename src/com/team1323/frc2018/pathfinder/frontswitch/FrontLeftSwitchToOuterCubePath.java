package com.team1323.frc2018.pathfinder.frontswitch;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.pathfinder.PathfinderPath;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class FrontLeftSwitchToOuterCubePath extends PathfinderPath{
	
	public FrontLeftSwitchToOuterCubePath(){
		super.points = new Waypoint[]{
			new Waypoint(Constants.kLeftSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 1.0, Constants.kLeftSwitchCloseCorner.y() + Constants.ROBOT_HALF_WIDTH + 1.0, Pathfinder.d2r(180)),
			new Waypoint(Constants.kLeftSwitchCloseCorner.x() - (3*Constants.kCubeWidth) - 1.75, Constants.kLeftSwitchCloseCorner.y() + Constants.ROBOT_HALF_WIDTH + 4.0, Pathfinder.d2r(90))
		};
		super.maxAccel = 8.0;
		super.defaultSpeed = 5.0;
		super.rotationScalar = 1.25;
		super.lookaheadPoints = 10;
		super.usePID = true;
	}
	
}
