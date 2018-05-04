package com.team1323.frc2018.pathfinder.frontswitch;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.pathfinder.PathfinderPath;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class FrontRightSwitchToOuterCubePath extends PathfinderPath{

	public FrontRightSwitchToOuterCubePath(){
		super.points = new Waypoint[]{
				new Waypoint(Constants.kRightSwitchCloseCorner.x() - Constants.kRobotHalfLength - 1.85, Constants.kRightSwitchCloseCorner.y() - Constants.kRobotHalfWidth - 1.0, Pathfinder.d2r(180)),
				new Waypoint(Constants.kRightSwitchCloseCorner.x() - Constants.kRobotHalfLength - 4.0, Constants.kRightSwitchCloseCorner.y() - Constants.kRobotHalfWidth - 1.0, Pathfinder.d2r(180)),
				new Waypoint(Constants.kRightSwitchCloseCorner.x() - (3*Constants.kCubeWidth) - 3.75, Constants.kRightSwitchCloseCorner.y() - Constants.kRobotHalfWidth - 2.5, Pathfinder.d2r(-90)),
				new Waypoint(Constants.kRightSwitchCloseCorner.x() - (3*Constants.kCubeWidth) - 2.25, Constants.kRightSwitchCloseCorner.y() - Constants.kRobotHalfWidth - 4.5, Pathfinder.d2r(0))
			};
			super.maxAccel = 5.0;
			super.defaultSpeed = 5.0;
			super.rotationScalar = 1.25;
			super.lookaheadPoints = 7;
			super.usePID = false;
	}
	
}
