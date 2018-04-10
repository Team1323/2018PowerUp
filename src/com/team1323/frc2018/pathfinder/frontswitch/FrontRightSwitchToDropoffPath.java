package com.team1323.frc2018.pathfinder.frontswitch;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.pathfinder.PathfinderPath;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class FrontRightSwitchToDropoffPath extends PathfinderPath{

	public FrontRightSwitchToDropoffPath(){
		super.points = new Waypoint[]{
			new Waypoint(Constants.kRightSwitchCloseCorner.x() - Constants.kRobotHalfLength - 0.75, Constants.kRightSwitchCloseCorner.y() - Constants.kRobotHalfWidth - 0.5, Pathfinder.d2r(90)),
			new Waypoint(Constants.kRightSwitchCloseCorner.x() - Constants.kRobotHalfLength - 0.75, Constants.kRightSwitchCloseCorner.y() - Constants.kRobotHalfWidth + 0.5, Pathfinder.d2r(90)),
			new Waypoint(Constants.kRightSwitchCloseCorner.x() + 3.0, Constants.kRightSwitchCloseCorner.y() + Constants.kRobotHalfWidth + 0.5, Pathfinder.d2r(0))
		};
		super.maxAccel = 5.0;
		super.defaultSpeed = 5.0;
		super.lookaheadPoints = 10;
	}
	
}
