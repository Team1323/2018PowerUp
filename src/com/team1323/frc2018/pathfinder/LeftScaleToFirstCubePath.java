package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class LeftScaleToFirstCubePath extends PathfinderPath{
	
	public LeftScaleToFirstCubePath(){
		super.points = new Waypoint[]{
				new Waypoint(Constants.kLeftScaleCorner.x() - Constants.kRobotHalfLength - 1.0, Constants.kLeftScaleCorner.y() - Constants.kRobotHalfWidth - 0.5, Pathfinder.d2r(-135)),
				new Waypoint(Constants.kLeftSwitchFarCorner.x() + 3.25, Constants.kLeftSwitchFarCorner.y() + 0.4, Pathfinder.d2r(90))
		};
		super.maxAccel = 5.0;
		super.defaultSpeed = 4.1;
		super.rotationScalar = 0.75;
		super.lookaheadPoints = 10;
	}
	
}
