package com.team1323.frc2018.pathfinder;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class RightScaleToSecondCubePath extends PathfinderPath{
	public RightScaleToSecondCubePath(){
		super.points = new Waypoint[]{
			new Waypoint(23.99, 20.3125, Pathfinder.d2r(180)),
			new Waypoint(20.5, 18.75, Pathfinder.d2r(-90)),
			new Waypoint(18.393, 17.187, Pathfinder.d2r(0))
		};
		super.maxAccel = 4;
	}
}
