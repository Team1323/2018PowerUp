package com.team1323.frc2018.auto.actions;

import com.team1323.frc2018.pathfinder.PathfinderPath;
import com.team1323.frc2018.subsystems.Swerve;

public class FollowPathAction extends RunOnceAction{
	PathfinderPath path;
	double goalHeading;
	Swerve swerve;
	
	public FollowPathAction(PathfinderPath path, double goalHeading){
		this.path = path;
		this.goalHeading = goalHeading;
		swerve = Swerve.getInstance();
	}
	
	@Override
	public synchronized void runOnce(){
		swerve.followPath(path, goalHeading);
	}
}
