package com.team1323.frc2018.pathfinder.frontswitch;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.pathfinder.PathfinderPath;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class OuterCubeToFrontRightSwitchPath extends PathfinderPath{

	public OuterCubeToFrontRightSwitchPath(){
		super.points = new Waypoint[]{
			new Waypoint(Constants.kRightSwitchCloseCorner.x() - (3*Constants.kCubeWidth) - 2.25, Constants.kRightSwitchCloseCorner.y() - Constants.ROBOT_HALF_WIDTH - 4.5, Pathfinder.d2r(120)),
			new Waypoint(Constants.kRightSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 1.25, Constants.kRightSwitchCloseCorner.y() - Constants.ROBOT_HALF_WIDTH - 0.5, Pathfinder.d2r(0))
		};
		super.maxAccel = 8.0;
		super.defaultSpeed = 5.0;
		super.rotationScalar = 1.0;
		super.lookaheadPoints = 15;
		super.usePID = false;
	}
	
}
