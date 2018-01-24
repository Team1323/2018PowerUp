package com.team1323.frc2018.pathfinder;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class RightCubeToLeftScalePath extends PathfinderPath{
	
	public RightCubeToLeftScalePath(){
		super.points = new Waypoint[]{
			new Waypoint(18.393, 19.354, Pathfinder.d2r(-45)),
			new Waypoint(19.06125, 13.5, Pathfinder.d2r(-90)),
			new Waypoint(23.99, 6.6875, Pathfinder.d2r(0))
		};
		super.maxAccel = 6;
	}
	
}
