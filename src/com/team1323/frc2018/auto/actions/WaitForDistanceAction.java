package com.team1323.frc2018.auto.actions;

import com.team1323.frc2018.pathfinder.PathfinderPath;
import com.team1323.frc2018.subsystems.Swerve;
import com.team254.lib.util.math.Translation2d;

public class WaitForDistanceAction implements Action{
	private Swerve swerve;
	private Translation2d goalPosition;
	private double distance;
	
	public WaitForDistanceAction(double distance, PathfinderPath path){
		swerve = Swerve.getInstance();
		goalPosition = path.getFinalPosition();
		this.distance = distance;
	}

	@Override
	public boolean isFinished() {
		return swerve.getPose().getTranslation().translateBy(goalPosition.inverse()).norm() <= distance;
	}

	@Override
	public void start() {
	}

	@Override
	public void update() {
	}

	@Override
	public void done() {
	}
	
}
