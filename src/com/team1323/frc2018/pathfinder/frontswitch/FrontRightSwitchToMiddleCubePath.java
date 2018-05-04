package com.team1323.frc2018.pathfinder.frontswitch;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.pathfinder.PathfinderPath;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class FrontRightSwitchToMiddleCubePath extends PathfinderPath{

	public FrontRightSwitchToMiddleCubePath(){
		super.points = new Waypoint[]{
				new Waypoint(Constants.kRightSwitchCloseCorner.x() - Constants.kRobotHalfLength - 1.1, Constants.kRightSwitchCloseCorner.y() - Constants.kRobotHalfWidth - 0.5, Pathfinder.d2r(180)),
				//new Waypoint(Constants.kRightSwitchCloseCorner.x() - (2*Constants.kCubeWidth) - 1.1, Constants.kRightSwitchCloseCorner.y() - Constants.ROBOT_HALF_WIDTH - 2.0, Pathfinder.d2r(-90)),
				new Waypoint(Constants.kRightSwitchCloseCorner.x() - (2*Constants.kCubeWidth) - 3.0, Constants.kRightSwitchCloseCorner.y() - Constants.kRobotHalfWidth - 2.75, Pathfinder.d2r(-90)),
				new Waypoint(Constants.kRightSwitchCloseCorner.x() - (2*Constants.kCubeWidth) - 1.5, Constants.kRightSwitchCloseCorner.y() - Constants.kRobotHalfWidth - 4.6, Pathfinder.d2r(0))	
			};
		super.maxAccel = 8.0;
		super.defaultSpeed = 4.0;
		super.rotationScalar = 1.5;
		super.lookaheadPoints = 7;
		super.usePID = false;
	}
	
}
