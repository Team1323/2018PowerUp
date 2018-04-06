package com.team1323.frc2018.pathfinder.frontswitch;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.pathfinder.PathfinderPath;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class FrontLeftSwitchToDropoffPath extends PathfinderPath{
	
	public FrontLeftSwitchToDropoffPath(){
		super.points = new Waypoint[]{
			//new Waypoint(Constants.kLeftSwitchCloseCorner.x() - (2*Constants.kCubeWidth) - 1.4, Constants.kLeftSwitchCloseCorner.y() + Constants.kRobotHalfWidth + 5.4, Pathfinder.d2r(-90)),
			//new Waypoint(Constants.kLeftSwitchCloseCorner.x() - (2*Constants.kCubeWidth) - 1.4, Constants.kLeftSwitchCloseCorner.y() + Constants.kRobotHalfWidth + 0.0, Pathfinder.d2r(-90)),
			new Waypoint(Constants.kLeftSwitchCloseCorner.x() - Constants.kRobotHalfLength - 0.25, Constants.kLeftSwitchCloseCorner.y() + Constants.kRobotHalfWidth + 0.5, Pathfinder.d2r(-90)),
			new Waypoint(Constants.kLeftSwitchCloseCorner.x() - Constants.kRobotHalfLength - 0.25, Constants.kLeftSwitchCloseCorner.y() + Constants.kRobotHalfWidth - 1.5, Pathfinder.d2r(-90)),
			new Waypoint(Constants.kLeftSwitchCloseCorner.x() + 3.0, Constants.kLeftSwitchCloseCorner.y() - Constants.kRobotHalfWidth - 0.5, Pathfinder.d2r(0))
		};
		super.maxAccel = 5.0;
		super.defaultSpeed = 5.0;
		super.lookaheadPoints = 10;
		super.rotationScalar = 0.5;
	}
	
}
