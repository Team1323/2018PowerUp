package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class SecondRightCubeToScalePath extends PathfinderPath{
	
	public SecondRightCubeToScalePath(){
		super.points = new Waypoint[]{
				new Waypoint(Constants.kRightSwitchFarCorner.x() + 3.6, Constants.kRightSwitchFarCorner.y() + Constants.kRobotHalfLength - 2.5, Pathfinder.d2r(60.0)),
				new Waypoint(23.25, 21.5, Pathfinder.d2r(0.0))//23.25 22.5
			};
		super.maxAccel = 6.0;
		super.defaultSpeed = 5.0;
		super.rotationScalar = 1.0;
		super.lookaheadPoints = 20;
	}
	
}
