package com.team1323.frc2018.pathfinder;

import com.team254.lib.util.math.RigidTransform2d;
import com.team254.lib.util.math.Rotation2d;
import com.team254.lib.util.math.Translation2d;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Segment;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.DistanceFollower;

public class PathfinderPath {
	protected double maxSpeed = 10.0;
	protected double maxAccel = 10.0;
	protected double maxJerk = 84.0;
	protected double dt = 0.01;
	protected int samples = Trajectory.Config.SAMPLES_LOW;
	protected double p = 1.0;
	protected double d = 0.0;
	protected double v = 1.0/13.89;
	protected double a = 0.0;
	protected int lookaheadPoints = 6;
	private Segment lastSegment;
	private Translation2d desiredFinalPosition;
	
	protected Waypoint[] points = null;
	private Trajectory trajectory;
	private DistanceFollower follower; 
	
	public void buildPath(){
		Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, samples, dt, maxSpeed, maxAccel, maxJerk);
		trajectory = Pathfinder.generate(points, config);
		lastSegment = trajectory.get(trajectory.length() - 1);
		desiredFinalPosition = new Translation2d(lastSegment.x, lastSegment.y);
		resetFollower();
	}
	
	public int getLookaheadPoints(){
		return lookaheadPoints;
	}
	
	public void setLookaheadDistance(double feet){
		lookaheadPoints = (int) (feet / (maxSpeed*dt));
	}
	
	public Trajectory getTrajectory(){
		return trajectory;
	}
	
	public DistanceFollower resetFollower(){
		follower = new DistanceFollower(trajectory);
		follower.configurePIDVA(p, 0.0, d, v, a);
		return follower;
	}
	
	public Translation2d getFinalPosition(){
		return desiredFinalPosition;
	}
	
	public double runPID(double error){
		return error * p + v * lastSegment.velocity;
	}
}
