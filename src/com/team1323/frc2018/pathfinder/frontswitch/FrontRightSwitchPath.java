package com.team1323.frc2018.pathfinder.frontswitch;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.pathfinder.PathfinderPath;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class FrontRightSwitchPath extends PathfinderPath{
	
	public FrontRightSwitchPath(){
		super.points = new Waypoint[]{
				new Waypoint(Constants.ROBOT_HALF_LENGTH, Constants.kAutoStartingCorner.y() + Constants.ROBOT_HALF_WIDTH, Pathfinder.d2r(50)),
				new Waypoint(Constants.kRightSwitchCloseCorner.x() - Constants.ROBOT_HALF_LENGTH - 2.0, Constants.kRightSwitchCloseCorner.y() - Constants.ROBOT_HALF_WIDTH - 1.0, Pathfinder.d2r(0)),
			};
		super.maxAccel = 6.0;
		super.defaultSpeed = 6.3;
		super.usePID = true;
		super.lookaheadPoints = 20;
	}
	
}
