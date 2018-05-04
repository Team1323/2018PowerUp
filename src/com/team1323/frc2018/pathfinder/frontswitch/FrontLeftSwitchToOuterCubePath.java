package com.team1323.frc2018.pathfinder.frontswitch;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.pathfinder.PathfinderPath;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class FrontLeftSwitchToOuterCubePath extends PathfinderPath{
	
	public FrontLeftSwitchToOuterCubePath(){
		super.points = new Waypoint[]{
			new Waypoint(Constants.kLeftSwitchCloseCorner.x() - Constants.kRobotHalfLength - 1.85, Constants.kLeftSwitchCloseCorner.y() + Constants.kRobotHalfWidth + 1.0, Pathfinder.d2r(180)),
			new Waypoint(Constants.kLeftSwitchCloseCorner.x() - Constants.kRobotHalfLength - 3.0, Constants.kLeftSwitchCloseCorner.y() + Constants.kRobotHalfWidth + 1.0, Pathfinder.d2r(180)),
			new Waypoint(Constants.kLeftSwitchCloseCorner.x() - (3*Constants.kCubeWidth) - 3.5, Constants.kLeftSwitchCloseCorner.y() + Constants.kRobotHalfWidth + 2.7, Pathfinder.d2r(90)),
			new Waypoint(Constants.kLeftSwitchCloseCorner.x() - (3*Constants.kCubeWidth) - 2.0, Constants.kLeftSwitchCloseCorner.y() + Constants.kRobotHalfWidth + 5.1, Pathfinder.d2r(0))
		};
		super.maxAccel = 5.0;
		super.defaultSpeed = 5.0;
		super.rotationScalar = 1.25;
		super.lookaheadPoints = 7;
		super.usePID = false;
	}
	
}
