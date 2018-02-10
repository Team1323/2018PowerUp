package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class RightCubeToRightScalePath extends PathfinderPath{

	public RightCubeToRightScalePath(){
		super.points = new Waypoint[]{
			new Waypoint(Constants.kRightSwitchFarCorner.x() + Constants.ROBOT_INTAKE_EXTRUSION + Constants.ROBOT_HALF_LENGTH + 1.0, Constants.kRightSwitchFarCorner.y() - (Constants.kCubeWidth/2.0) + 0.75, Pathfinder.d2r(30)),
			new Waypoint(Constants.kRightScaleCorner.x() - Constants.ROBOT_HALF_LENGTH - 0.5, Constants.kRightScaleCorner.y() + Constants.ROBOT_HALF_WIDTH, Pathfinder.d2r(0))
		};
		super.maxAccel = 15.0;
	}
	
}
