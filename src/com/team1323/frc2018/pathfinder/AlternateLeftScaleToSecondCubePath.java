package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class AlternateLeftScaleToSecondCubePath extends PathfinderPath{

	public AlternateLeftScaleToSecondCubePath(){
		super.points = new Waypoint[]{
			new Waypoint(22.75, 5.75, Pathfinder.d2r(180)),
			new Waypoint(Constants.kLeftSwitchFarCorner.x() + 3.15/*2.75*/, Constants.kLeftSwitchFarCorner.y() + 3.0, Pathfinder.d2r(90))
		};
		super.maxAccel = 5.0;
		super.defaultSpeed = 4.5;
		super.rotationScalar = 0.75;
		super.lookaheadPoints = 15;
	}
	
}
