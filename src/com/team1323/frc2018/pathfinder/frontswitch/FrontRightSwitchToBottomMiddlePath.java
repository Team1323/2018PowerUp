package com.team1323.frc2018.pathfinder.frontswitch;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.pathfinder.PathfinderPath;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class FrontRightSwitchToBottomMiddlePath extends PathfinderPath{

	public FrontRightSwitchToBottomMiddlePath(){
		super.points = new Waypoint[]{
				new Waypoint(Constants.kRightSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 1.0, Constants.kRightSwitchCloseCorner.y() - Constants.ROBOT_HALF_WIDTH - 0.5, Pathfinder.d2r(180)),
				new Waypoint(Constants.kRightSwitchCloseCorner.x() - (2*Constants.kCubeWidth) - 1.25, Constants.kRightSwitchCloseCorner.y() - Constants.ROBOT_HALF_WIDTH - 3.25, Pathfinder.d2r(-90))
		};
		super.maxAccel = 9.0;
		super.defaultSpeed = 4.3;
		super.rotationScalar = 1.0;
		super.lookaheadPoints = 15;
		super.usePID = true;
	}
	
}
