package com.team1323.frc2018.pathfinder.frontswitch;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.pathfinder.PathfinderPath;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class FrontRightSwitchPath extends PathfinderPath{
	
	public FrontRightSwitchPath(){
		super.points = new Waypoint[]{
				new Waypoint(Constants.kRobotHalfLength, Constants.kAutoStartingCorner.y() + Constants.kRobotHalfWidth, Pathfinder.d2r(50)),
				new Waypoint(Constants.kRightSwitchCloseCorner.x() - Constants.kRobotHalfLength - 1.85, Constants.kRightSwitchCloseCorner.y() - Constants.kRobotHalfWidth - 1.0, Pathfinder.d2r(0)),
			};
		super.maxAccel = 6.0;
		super.defaultSpeed = 6.3;
		super.usePID = false;
		super.lookaheadPoints = 20;
	}
	
}
