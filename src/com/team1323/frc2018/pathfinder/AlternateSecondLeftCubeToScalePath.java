package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class AlternateSecondLeftCubeToScalePath extends PathfinderPath{

	public AlternateSecondLeftCubeToScalePath(){
		super.points = new Waypoint[]{
			new Waypoint(Constants.kLeftSwitchFarCorner.x() + 3.0, Constants.kLeftSwitchFarCorner.y() + 2.0, Pathfinder.d2r(-60)),
			new Waypoint(22.75, 5.75, Pathfinder.d2r(0)),
		};
		super.maxAccel = 5.0;
		super.defaultSpeed = 4.5;
		super.rotationScalar = 0.75;
		super.lookaheadPoints = 20;
	}
	
}
