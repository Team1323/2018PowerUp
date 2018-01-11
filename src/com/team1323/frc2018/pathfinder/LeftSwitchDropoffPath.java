package com.team1323.frc2018.pathfinder;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class LeftSwitchDropoffPath extends PathfinderPath{
	
	public LeftSwitchDropoffPath(){
		super.points = new Waypoint[]{
				new Waypoint(0,0,Pathfinder.d2r(-50)),
				new Waypoint(10.0, -7.5, 0),
				new Waypoint(16.25, -7.5, 0),
				new Waypoint(18.75, -5.0, Pathfinder.d2r(45))
		};
	}
	
}
