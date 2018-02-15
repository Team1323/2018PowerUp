package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class SecondRightCubeToScalePath extends PathfinderPath{
	
	public SecondRightCubeToScalePath(){
		super.points = new Waypoint[]{
				new Waypoint(18.75, 19.0, Pathfinder.d2r(70.0)),
				new Waypoint(Constants.kRightScaleCorner.x() - Constants.ROBOT_HALF_LENGTH - 2.0, Constants.kRightScaleCorner.y() + Constants.ROBOT_HALF_WIDTH + 1.0, Pathfinder.d2r(0))
			};
		super.maxAccel = 5.0;
		super.defaultSpeed = 4.5;
		super.rotationScalar = 0.75;
	}
	
}
