package com.team1323.frc2018.pathfinder.frontswitch;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.pathfinder.PathfinderPath;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class FrontLeftSwitchToMiddleCubePath extends PathfinderPath{

	public FrontLeftSwitchToMiddleCubePath(){
		super.points = new Waypoint[]{
			new Waypoint(Constants.kLeftSwitchCloseCorner.x() - Constants.kRobotHalfLength - 1.1, Constants.kLeftSwitchCloseCorner.y() + Constants.kRobotHalfWidth + 1.25, Pathfinder.d2r(180)),
			//new Waypoint(Constants.kLeftSwitchCloseCorner.x() - (2*Constants.kCubeWidth) - 1.25, Constants.kLeftSwitchCloseCorner.y() + Constants.ROBOT_HALF_WIDTH + 3.0, Pathfinder.d2r(90)),
			new Waypoint(Constants.kLeftSwitchCloseCorner.x() - (2*Constants.kCubeWidth) - 3.0, Constants.kLeftSwitchCloseCorner.y() + Constants.kRobotHalfWidth + 3.1, Pathfinder.d2r(90)),
			new Waypoint(Constants.kLeftSwitchCloseCorner.x() - (2*Constants.kCubeWidth) - 1.5, Constants.kLeftSwitchCloseCorner.y() + Constants.kRobotHalfWidth + 5.2, Pathfinder.d2r(0))	
		};
		super.maxAccel = 8.0;
		super.defaultSpeed = 4.0;//4.9
		super.rotationScalar = 1.5;
		super.lookaheadPoints = 7;
		super.usePID = false;
	}
	
}
