package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class LeftScaleToFirstCubePath extends PathfinderPath{
	
	public LeftScaleToFirstCubePath(){
		super.points = new Waypoint[]{
			new Waypoint(Constants.kLeftScaleCorner.x() - Constants.ROBOT_HALF_LENGTH - 1.0, Constants.kLeftScaleCorner.y() - Constants.ROBOT_HALF_WIDTH, Pathfinder.d2r(0)),
			new Waypoint(Constants.kLeftMostCube.x(), Constants.kLeftMostCube.y(), Pathfinder.d2r(150))
		};
		super.maxSpeed = 8.0;
		super.maxAccel = 5.0;
		super.defaultSpeed = 4.8;
	}
	
}
