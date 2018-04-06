package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class LeftCubeToLeftScalePath extends PathfinderPath{
	
	public LeftCubeToLeftScalePath(){
		super.points = new Waypoint[]{
				new Waypoint(Constants.kLeftSwitchFarCorner.x() + 3.6, Constants.kLeftSwitchFarCorner.y() + 0.4, Pathfinder.d2r(-30)),
				new Waypoint(23.0, 5.75, Pathfinder.d2r(0))
		};
		super.maxSpeed = 8.0;
		super.maxAccel = 5.0;
		super.defaultSpeed = 3.5;
		super.rotationScalar = 0.75;
		super.lookaheadPoints = 20;
		super.rotationOverride = false;
	}
	
}
