package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class DerpLeftCubeToLeftScalePath extends PathfinderPath{

	public DerpLeftCubeToLeftScalePath(){
		super.points = new Waypoint[]{
			new Waypoint(Constants.kLeftSwitchFarCorner.x() + 3.6, Constants.kLeftSwitchFarCorner.y() + 0.4, Pathfinder.d2r(-30)),
			new Waypoint(22.75, 5.75, Pathfinder.d2r(0))
		};
		super.maxSpeed = 8.0;
		super.maxAccel = 7.0;
		super.defaultSpeed = 4.5;
		super.rotationScalar = 0.9;
		super.lookaheadPoints = 20;
		super.rotationOverride = false;
	}
	
}
