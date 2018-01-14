package com.team1323.frc2018.loops;

import com.team1323.frc2018.RobotState;
import com.team1323.frc2018.subsystems.Swerve;

public class RobotStateEstimator implements Loop{
	private static RobotStateEstimator instance = null;
	public static RobotStateEstimator getInstance(){
		if(instance == null)
			instance = new RobotStateEstimator();
		return instance;
	}
	
	RobotStateEstimator(){
	}
	
	RobotState robotState = RobotState.getInstance();
	Swerve swerve = Swerve.getInstance();

	@Override
	public void onStart(double timestamp) {
		
	}

	@Override
	public void onLoop(double timestamp) {
		
	}

	@Override
	public void onStop(double timestamp) {
		
	}

}
