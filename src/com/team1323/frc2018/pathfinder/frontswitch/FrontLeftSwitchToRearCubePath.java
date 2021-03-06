package com.team1323.frc2018.pathfinder.frontswitch;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.pathfinder.PathfinderPath;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class FrontLeftSwitchToRearCubePath extends PathfinderPath{

	public FrontLeftSwitchToRearCubePath(){
		super.points = new Waypoint[]{
			new Waypoint(Constants.kLeftSwitchCloseCorner.x() - Constants.kRobotHalfLength - 0.5, Constants.kLeftSwitchCloseCorner.y() + Constants.kRobotHalfWidth + 1.0, Pathfinder.d2r(180)),
			new Waypoint(Constants.kLeftSwitchCloseCorner.x() - (1*Constants.kCubeWidth) - 1.5, Constants.kLeftSwitchCloseCorner.y() + Constants.kRobotHalfWidth + 4.0, Pathfinder.d2r(45))	
		};
		super.maxAccel = 8.0;
		super.defaultSpeed = 4.9;
		super.rotationScalar = 1.5;
		super.lookaheadPoints = 10;
		super.usePID = true;
	}
	
}
