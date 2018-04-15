package com.team1323.frc2018.pathfinder.frontswitch;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.pathfinder.PathfinderPath;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class FrontRightSwitchToBottomMiddlePath extends PathfinderPath{

	public FrontRightSwitchToBottomMiddlePath(){
		super.points = new Waypoint[]{
				new Waypoint(Constants.kRightSwitchCloseCorner.x() - Constants.kRobotHalfLength - 1.0, Constants.kRightSwitchCloseCorner.y() - Constants.kRobotHalfWidth - 0.5, Pathfinder.d2r(-120)),
				new Waypoint(Constants.kRightSwitchCloseCorner.x() - (2*Constants.kCubeWidth) - 1.5, Constants.kRightSwitchCloseCorner.y() - Constants.kRobotHalfWidth - 3.25, Pathfinder.d2r(-90))
		};
		super.maxAccel = 9.0;
		super.defaultSpeed = 4.3;
		super.rotationScalar = 1.0;
		super.lookaheadPoints = 15;
		super.usePID = true;
	}
	
}
