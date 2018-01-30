package com.team1323.frc2018.pathfinder;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class RightSwitchDropoffPath extends PathfinderPath{
	
	public RightSwitchDropoffPath(){
		super.points = new Waypoint[]{
				new Waypoint(11.75/12.0, 13.75,Pathfinder.d2r(50)),
				new Waypoint(11.667, 20.875, 0),
				new Waypoint(16.33, 20.875, 0),
				//new Waypoint(19.726, 19.354, Pathfinder.d2r(-45)),
				new Waypoint(20.25, 20.49475, Pathfinder.d2r(-45)),
				new Waypoint(20.25, 19.73425, Pathfinder.d2r(-135)),
				new Waypoint(18.393, 19.354, Pathfinder.d2r(180))
		};
		super.maxAccel = 10.0;
	}
	
}
