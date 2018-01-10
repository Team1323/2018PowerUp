package com.team1323.frc2018.pathfinder;

import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

public interface PathfinderPath {
	Waypoint[] getPoints();
	
	Trajectory getTrajectory();
	
	void buildPath();
}
