package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class LeftScaleToSecondCubePath extends PathfinderPath{

	public LeftScaleToSecondCubePath(){
		super.points = new Waypoint[]{
			//new Waypoint(Constants.kLeftScaleCorner.x() - Constants.ROBOT_HALF_LENGTH - 1.5, Constants.kLeftScaleCorner.y() - Constants.ROBOT_HALF_WIDTH - 1.0, Pathfinder.d2r(0)),
			//new Waypoint(23.5, 5.75, Pathfinder.d2r(0)),
			//new Waypoint(19.0, 8.5, Pathfinder.d2r(135.0))
			new Waypoint(23.25, 5.75, Pathfinder.d2r(180)),
			//new Waypoint(Constants.kLeftSwitchFarCorner.x() + 2.5, Constants.kLeftSwitchFarCorner.y(), Pathfinder.d2r(90)),
			new Waypoint(Constants.kLeftSwitchFarCorner.x() + 3.0, Constants.kLeftSwitchFarCorner.y() + 3.0, Pathfinder.d2r(90))
		};
		super.maxAccel = 5.0;
		super.defaultSpeed = 4.5;
		super.rotationScalar = 0.5;
		super.lookaheadPoints = 15;
	}
	
}
