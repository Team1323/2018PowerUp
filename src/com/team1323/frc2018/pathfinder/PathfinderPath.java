package com.team1323.frc2018.pathfinder;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

public class PathfinderPath {
	protected double maxSpeed = 10.0;
	protected double maxAccel = 6.0;
	protected double maxJerk = 84.0;
	protected double dt = 0.02;
	protected int samples = Trajectory.Config.SAMPLES_LOW;
	
	public Waypoint[] points = null;
	
	public Trajectory trajectory;
	
	public void buildPath(){
		Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, samples, dt, maxSpeed, maxAccel, maxJerk);
		trajectory = Pathfinder.generate(points, config);
	}
	
	public Trajectory getTrajectory(){
		return trajectory;
	}
}
