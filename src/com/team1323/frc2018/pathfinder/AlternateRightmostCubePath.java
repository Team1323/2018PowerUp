package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class AlternateRightmostCubePath extends PathfinderPath{

	public AlternateRightmostCubePath(){
		super.points = new Waypoint[]{
			new Waypoint(Constants.kRightScaleCorner.x() - Constants.ROBOT_HALF_LENGTH - 0.75, Constants.kRightScaleCorner.y() + Constants.ROBOT_HALF_WIDTH + 3.0, Pathfinder.d2r(-135)),
			new Waypoint(Constants.kRightScaleCorner.x() - Constants.ROBOT_HALF_LENGTH - 3.0, Constants.kRightScaleCorner.y() + Constants.ROBOT_HALF_WIDTH + 2.0, Pathfinder.d2r(-135))
		};
		super.defaultSpeed = 4.0;
		super.maxSpeed = 4.0;
		super.maxAccel = 10.0;
		super.rotationScalar = 0.5;
		super.lookaheadPoints = 10;
	}
	
}
