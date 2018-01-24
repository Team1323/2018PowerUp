package com.team1323.frc2018.pathfinder;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class RightScaleToFirstCubePath extends PathfinderPath{

	public RightScaleToFirstCubePath(){
		super.points = new Waypoint[]{
			new Waypoint(23.99, 20.3125, Pathfinder.d2r(180)),
			new Waypoint(18.393, 19.354, Pathfinder.d2r(0))
		};
	}
	
}
