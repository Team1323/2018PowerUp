package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class StartToLeftScalePath extends PathfinderPath{

	public StartToLeftScalePath(){
		super.points = new Waypoint[]{
			new Waypoint(Constants.ROBOT_HALF_LENGTH, Constants.kAutoStartingCorner.y() + Constants.ROBOT_HALF_WIDTH,Pathfinder.d2r(-50)),
			new Waypoint(Constants.kLeftSwitchCloseCorner.x() - 1.0, Constants.kLeftSwitchCloseCorner.y() - Constants.ROBOT_HALF_LENGTH - 1.0, Pathfinder.d2r(0.0)),
			new Waypoint(22.75, Constants.kLeftSwitchCloseCorner.y() - Constants.ROBOT_HALF_LENGTH - 1.5, Pathfinder.d2r(0))
		};
		super.maxAccel = 2.0;
		super.maxSpeed = 8.0;
		super.dt = 0.02;
		super.lookaheadPoints = 20;
		super.defaultSpeed = 7.0;
		super.rotationScalar = 0.5;
	}
	
}
