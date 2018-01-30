package com.team1323.frc2018.pathfinder;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class RightCubeToRightScalePath extends PathfinderPath{

	public RightCubeToRightScalePath(){
		super.points = new Waypoint[]{
			new Waypoint(18.393, 19.354, Pathfinder.d2r(30)),
			new Waypoint(23.99, 20.3125, Pathfinder.d2r(0))
		};
		super.maxAccel = 6.0;
	}
	
}
